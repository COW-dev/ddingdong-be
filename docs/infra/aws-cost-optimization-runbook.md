# AWS 비용 절감 + Graviton 전환 + 모니터링 CloudWatch 이전 런북

목표: prod AWS 월 비용 ~17만원 → **10만원 이하** (실측 기반 목표 ~$54 ≈ 7.3만원).
핵심 전략: **stop 해도 안 내려가는 고정비 제거** (IPv4·ALB·자체 모니터링 서버) + Graviton(t4g) 전환 + dev 시간제 스케줄.

> 실제 계정 ID·리소스 ID·ARN 등 민감 식별자는 공개 레포에 두지 않는다.
> 실행 로그와 구체 ID는 로컬 전용 `docs/local/aws-cost-execution-log.md`(gitignored) 참조.

---

## ⚠️ 실행 전 필수 — 계정 확인 (PREFLIGHT)

이 작업의 모든 AWS 명령은 **prod 계정**에서 실행해야 한다.

> 로컬 `~/.aws` 기본 프로파일이 prod와 **무관한 계정**을 가리키는 경우가 있었다.
> 잘못된 계정에서 release/terminate 실행 시 무관한(또는 live) 리소스를 파괴한다.

모든 phase 착수 전 반드시:

```bash
aws sts get-caller-identity      # prod 계정 번호가 맞는지 확인
aws configure get region         # ap-northeast-2 인지 확인
```

- 자격증명은 `.env`의 prod 키에 대응하는 전용 프로파일로 분리해 `--profile <prod>` 로 사용.
- **리소스 ID(EIP allocation-id, ENI, 인스턴스 id 등)는 실행 시점에 반드시 재조회**해서 쓴다.

---

## 📌 실행 로그 요약 (2026-07-12 세션 — 구체 ID는 docs/local 참조)

완료:
- ✅ **Phase A (부분)**: 진짜 idle EIP 1개 release.
- ✅ **Phase B**: RDS `ddingdong-dev` → `db.t4g.micro` available. 재부팅 후 live 앱 헬스 200 재연결 확인.
- ✅ **IAM**: EB 인스턴스 역할에 `CloudWatchAgentServerPolicy` 부착 (Phase E 준비).
- ✅ **코드**: Phase E/D 변경 커밋+푸시.

plan 대비 **실측으로 수정된 전제 (중요)**:
1. **Phase A "고아 ENI 2개"는 고아가 아니었다.** 두 ENI 모두 Status=**in-use**, 설명이 **live EB ALB** 소속. 지금 삭제/release 하면 **서비스 다운**. → Phase C에서 ALB 제거 시 자연 소멸. Phase A 실제 절감 = idle 1개(−$3.6)뿐.
2. **ARM EB 관리형 플랫폼 없음.** ap-northeast-2 Corretto 21 EB 솔루션 스택은 전부 x86_64. **EB로 t4g 전환 불가.** 커스텀 ARM 플랫폼/Docker 아니면 prod EB는 x86 유지. 단 ALB 제거(single-instance)는 x86으로도 가능(주 절감 유효).
3. **TLS는 ALB(:443)가 ACM 인증서로 종료.** ACM 공인 인증서는 raw EC2 직접 부착 불가 → **ALB 제거 시 CloudFront/Let's Encrypt 등 대체 TLS + DNS 재지정 필수**. 미해결 시 HTTPS 다운.
4. **dev EC2 부재.** dev/prod 이전으로 별도 dev docker-compose 서버가 존재하지 않음(dev 계정 EC2 0개). → **Phase D(dev t4g)·F(dev 스케줄) 대상 없음** → 목적 재정의 필요.

→ 손실 없는 절감(idle EIP, RDS t4g)은 반영됨. 남은 큰 절감(ALB 제거)은 **TLS 재설계 결정** 없이는 불가.

---

## 비용 Before / After (월, USD 세전, 환율 1,350원 가정)

| 항목 | Before | After |
|---|---|---|
| prod EB EC2 | 18.7 (t3.small, ALB) | 15.0 (single-instance; t4g는 EB 불가로 x86 유지 시 ~18) |
| dev EC2 | (대상 부재) | — |
| RDS | 21.4 (t3.micro) | 19.0 (t4g.micro) ✅ |
| VPC IPv4 | 17.6 | idle 1개 제거 반영, 나머지는 ALB/서버 정리와 함께 |
| ELB (ALB) | 15.2 | 0 (single-instance 전환 시, TLS 재설계 후) |
| 자체 모니터링 EC2 | 8.4 | 2.4 (AMI 스냅샷만, terminate 후) |
| CloudWatch | 0 | 4.0 (로그+에이전트, JVM 제외) |

핵심 절감은 **ALB 제거 + 모니터링 폐지 + RDS/EC2 t4g**. dev 스케줄·IPv6는 대상/노력 이슈로 후순위.

---

## Phase E — 모니터링 CloudWatch 이전 (코드: ✅ 완료, 배포 대기)

이 브랜치 반영 코드:

| 파일 | 변경 |
|---|---|
| `build.gradle` | `micrometer-registry-prometheus` 제거, 중복 `sentry-logback:7.6.0` 제거(7.14.0 유지) |
| `application-prod.yml` | actuator 노출 `health,info`로 축소. prometheus export/엔드포인트/metrics 블록 제거. `:9090`은 헬스체크용 유지 |
| `application-dev.yml` | `logging.config: classpath:logback-dev.xml`, `health,info` 노출 |
| `logback-dev.xml` (신규) | dev도 `logs/application.log` RollingFile 생성 |
| `SecurityConfig.java` | `/actuator/prometheus`,`/metrics` permitAll 제거 (health만) |
| 삭제 | `alloy/`, `promtail/`, 루트 promtail 파일, Alloy/Promtail Runner 2종 |
| `prod-server-deployer.yml` | deploy zip에서 alloy/promtail 제외 |
| `.ebextensions/04-cloudwatch-agent.config` (신규) | prod CW Agent (로그 + CPU/mem/disk) |
| `compose-dev.yaml` + `cloudwatch/dev-cloudwatch-agent.json` (신규) | dev 로그 CW 전송 사이드카 |

### 배포 전 수동 조치
1. **IAM (prod EB 인스턴스 프로파일)**: `CloudWatchAgentServerPolicy` 부착 — ✅ 완료.
2. **IAM (dev)**: dev EC2 존재 시 로그 write 권한 역할. (현재 dev EC2 부재)
3. **OTel javaagent 처리** (중요): `appstart`가 `-javaagent`로 트레이스를 구 Alloy(localhost:4317)로 export. Alloy 제거 후 대상 소멸 → 에러 로그 우려. 신규 배포 시:
   - EB 환경변수 `OTEL_SDK_DISABLED=true`, 또는 `appstart`에서 `-javaagent` 제거.
4. **RDS 지표**: 네이티브 `AWS/RDS` CloudWatch 콘솔 조회.

### 검증
- CW Logs `/ddingdong/prod/application`, `/ddingdong/dev/application` 스트림 수신
- CWAgent 네임스페이스 CPU/mem/disk 수신
- 부팅 로그에 Alloy/Promtail Runner 없음, OTLP export 에러 없음

---

## Phase A — IPv4 낭비 정리 (무손실)

> **되돌릴 수 없음.** release 전 각 EIP가 정말 미사용인지 재확인.

```bash
# 미연결(Assoc/ENI/Instance = None) EIP만 release 후보
aws --profile <prod> ec2 describe-addresses \
  --query 'Addresses[].{IP:PublicIp,Alloc:AllocationId,Assoc:AssociationId,ENI:NetworkInterfaceId,Instance:InstanceId}' --output table

aws --profile <prod> ec2 release-address --allocation-id <idle-alloc-id>
```

⚠️ **ENI가 붙은 EIP는 반드시 ENI 설명(Description) 확인.** `ELB app/awseb--...` 이면 **live ALB 소속** → 절대 삭제 금지. Phase C에서 ALB 제거 시 자연 소멸.

```bash
aws --profile <prod> ec2 describe-network-interfaces --network-interface-ids <eni> \
  --query 'NetworkInterfaces[].{Status:Status,Desc:Description,Attach:Attachment.InstanceId}'
```

- ✅ 진짜 idle EIP 1개는 이미 release 완료.
- app-prod/모니터링 EIP는 각 서버 정리(Phase D/E)와 함께.

**롤백**: EIP release는 복구 불가(동일 IP 재획득 미보장). release 전 Route53/외부 화이트리스트 연결 여부 확인.

---

## Phase B — RDS Graviton 전환 ✅ 완료

```bash
aws --profile <prod> rds modify-db-instance \
  --db-instance-identifier ddingdong-dev \
  --db-instance-class db.t4g.micro --apply-immediately
```

- 재부팅 짧게 발생(앱 1~3분). 엔드포인트 DNS 불변 → `${DB_URL}` 무영향.
- 실행 후 앱 헬스 200 확인 완료.
- **롤백**: 동일 명령으로 `db.t3.micro` 재지정.

---

## Phase C — prod EB single-instance (ALB 제거) — ⛔ 결정 필요, 보류

현재 live: EB LoadBalanced 환경(t3.small, ALB, ACM TLS 종료).

**실측 제약**:
- ARM EB 관리형 플랫폼 없음 → **t4g는 EB로 불가** (커스텀 ARM 플랫폼/Docker 또는 EB 탈피 필요).
- TLS를 ALB가 ACM으로 종료 → **single-instance 전환 시 대체 TLS 필수**.

**옵션(유저 결정)**:
| 옵션 | ALB 제거 | t4g | TLS | 리스크 |
|---|---|---|---|---|
| (a) single-instance x86 + CloudFront TLS | ✅ | ✗ | CloudFront(ACM us-east-1) 앞단 종료 | 중 (CF+DNS) |
| (b) single-instance x86 + nginx Let's Encrypt | ✅ | ✗ | 인스턴스 nginx 인증서 | 중 (갱신 운영) |
| (c) ALB 유지 + 인스턴스 다운사이즈 | ✗ | ✗ | 현행 | 저 (절감 작음) |
| (d) EB 탈피 → EC2 t4g + Docker + CloudFront | ✅ | ✅ | CloudFront | 고 (재구축) |

절차(옵션 확정 후): 신규 env/구성 → 헬스 Green + `/server/actuator/health` 200 + 실트래픽 검증 → **CNAME/DNS swap** → 구 env terminate.
**롤백**: swap 즉시 역전 가능. 구 env는 검증 전 terminate 금지.

---

## Phase D — dev EC2 Graviton + 이름 정리 — ⛔ 대상 부재, 보류

- 워크플로우 ARM 전환은 코드로 완료(`buildx --platform linux/arm64`, docker-compose `uname -m` 동적).
- 그러나 **dev 계정에 EC2가 없음** → 전환할 dev 서버 자체가 부재. dev/prod 이전으로 통합된 것으로 보임.
- **유저 확인 필요**: dev 서버 운영 방식(현재 어디서 도는지) 확정 후 Phase D/F 재정의.

---

## Phase F — dev EC2 시간제 스케줄링 — ⛔ 대상 부재, 보류

dev EC2 존재 시 EventBridge Scheduler 2개(12시 start / 0시 stop, KST)로 12h 가동. 현재 대상 부재.

---

## Phase G — IPv4 → IPv6 (선택, 고노력)

절감 $7.2/월 대비 네트워크 재구성 리스크 큼. dualstack 유지가 현실적일 수 있음(IPv4 요금 일부 잔존). 여유 시 별도 진행.

---

## Phase H — 보안 정리 (권장, 우선순위↑)

- `.env`에 **prod·dev AWS 키가 둘 다 root 액세스 키**. root 키는 존재 자체가 위험.
- 이번 작업에 prod root 키를 사용함 → **작업 종료 후 즉시 IAM 최소권한 유저로 로테이션 + root 키 삭제** 강력 권장.
- 필요 권한: CloudWatch/EC2/RDS/EB. 전용 IAM 유저 발급.

---

## 모니터링 EC2 폐지 (E 배포·검증 후)

```bash
aws --profile <prod> ec2 create-image --instance-id <monitoring-id> \
  --name "ddingdong-monitoring-backup-YYYYMMDD" --no-reboot
# AMI available 확인 후
aws --profile <prod> ec2 terminate-instances --instance-ids <monitoring-id>
aws --profile <prod> ec2 release-address --allocation-id <monitoring-eip-alloc>
```

**롤백**: AMI에서 `run-instances --image-id <ami>` 복원.

---

## 최종 검증 체크리스트

- [ ] `aws ce get-cost-and-usage` (1~2일 후): VPC/ELB 항목 급감
- [ ] prod EB 헬스 Green, `/server/actuator/health` 200, DNS swap 후 도메인 정상
- [ ] t4g 대상은 `uname -m`=`aarch64`, 앱 정상 기동, **한글 배너 폰트 렌더링**(noto-cjk arm64)
- [x] RDS `db.t4g.micro` available, 앱 DB 연결 정상
- [ ] CW Logs 수신, CWAgent 서버스펙 수신, `AWS/RDS` 지표 조회
- [ ] Alloy/Promtail 미기동, 부팅 에러 없음, monitoring terminate 후 영향 없음
