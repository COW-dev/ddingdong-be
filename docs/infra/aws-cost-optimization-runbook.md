# AWS 비용 절감 + Graviton 전환 + 모니터링 CloudWatch 이전 런북

목표: prod AWS 월 비용 ~17만원 → **10만원 이하** (실측 기반 목표 ~$54 ≈ 7.3만원).
핵심 전략: **stop 해도 안 내려가는 고정비 제거** (IPv4·ALB·자체 모니터링 서버) + Graviton(t4g) 전환 + dev 시간제 스케줄.

---

## ⚠️ 실행 전 필수 — 계정 확인 (PREFLIGHT)

이 작업의 모든 AWS 명령은 **prod 계정 `531444319321`** 에서 실행해야 한다.

> **주의**: 세션 최초 확인 시 로컬 `default` 프로파일이 계정 `776735194358`(root)로 잡혀 있었고,
> 그 계정에는 plan의 EIP(`15.164.132.178` 등)도, RDS `ddingdong-dev`도 없었다(DBInstanceNotFound).
> 즉 **로컬 default 프로파일 ≠ 대상 prod 계정**. 잘못된 계정에서 release/terminate 실행 시 무관한(또는 live) 리소스를 파괴한다.

모든 phase 착수 전 반드시:

```bash
aws sts get-caller-identity      # Account 가 531444319321 인지 확인
aws configure get region         # ap-northeast-2 인지 확인
```

계정이 다르면 올바른 프로파일 지정 후 진행:

```bash
export AWS_PROFILE=<prod-profile>   # 또는 aws sso login --profile <prod-profile>
```

**본 런북의 리소스 ID(EIP allocation-id, 인스턴스 id 등)는 실행 시점에 반드시 재조회**해서 쓴다. 아래 명령에 박힌 값은 예시가 아니라 plan 조사 시점 스냅샷이므로 그대로 신뢰 금지.

---

## 비용 Before / After (월, USD 세전, 환율 1,350원 가정)

| 항목 | Before | After |
|---|---|---|
| prod EB EC2 | 18.7 (t3.small, ALB) | 15.0 (t4g.small, single-instance) |
| dev EC2 | ~15 (t3, 24/7) | 7.5 (t4g.small, 12h/day) |
| RDS | 21.4 (t3.micro) | 19.0 (t4g.micro) |
| VPC IPv4 (5개) | 17.6 | 0 (IPv6) / 7.2 (dualstack 유지 시) |
| ELB (ALB) | 15.2 | 0 |
| 자체 모니터링 EC2 | 8.4 (compute+EBS) | 2.4 (AMI 스냅샷만) |
| CloudWatch | 0 | 4.0 (로그+에이전트, JVM 제외) |
| Route53/S3/기타 | ~5 | ~5.9 |
| **합계** | **~$86 (≈12만원)** | **~$54 (≈7.3만원)** |

IPv6 생략 시 ~$61 (8.3만원). dev 24/7 유지 시 ~$61. **둘 중 하나만 해도 목표 달성.**

---

## 실행 순서 (의존성)

```
A (IPv4 낭비 정리, 무손실)  ─┐
                            ├─► E (모니터링→CloudWatch 코드/에이전트 — 완료됨, 배포 필요)
B (RDS t4g)  ───────────────┘
       │
       ▼
C (prod EB Blue/Green, E 반영본 배포)
       │
       ▼
D (dev EC2 t4g + 이름 정리)
       │
       ▼
검증 → 모니터링 EC2 AMI + terminate
       │
       ▼
F (dev 시간제 스케줄) → G (IPv6, 선택) → H (root 키 로테이션)
```

---

## Phase E — 모니터링 CloudWatch 이전 (코드: ✅ 완료)

이 브랜치에서 이미 반영된 코드 변경:

| 파일 | 변경 |
|---|---|
| `build.gradle` | `micrometer-registry-prometheus` 제거, 중복 `sentry-logback:7.6.0` 제거(7.14.0 유지) |
| `application-prod.yml` | actuator 노출을 `health,info`로 축소. prometheus export/엔드포인트/metrics 블록 제거. `:9090` 포트는 헬스체크용 유지 |
| `application-dev.yml` | `logging.config: classpath:logback-dev.xml` 추가, `health,info` 노출 |
| `logback-dev.xml` (신규) | dev도 `logs/application.log` RollingFile 생성 (CW Agent tail 용) |
| `SecurityConfig.java` | `/server/actuator/prometheus`, `/metrics` permitAll 제거 (health만 유지) |
| 삭제 | `alloy/`, `promtail/`, 루트 `promtail-docker-compose.yml`/`promtail-config.yml`, `AlloyDockerComposeRunner.java`, `PromtailDockerComposeRunner.java` |
| `prod-server-deployer.yml` | deploy zip에서 `alloy`/`promtail` 복사 제거 |
| `.ebextensions/04-cloudwatch-agent.config` (신규) | prod EB에 CW Agent 설치 + 로그(`logs/application.log`)·서버스펙(CPU/mem/disk/procs) 수집 |
| `compose-dev.yaml` + `cloudwatch/dev-cloudwatch-agent.json` (신규) | dev EC2 CW Agent 사이드카(로그 전송), `app-logs` 볼륨 공유 |

### 배포 전 필요한 수동 조치 (AWS 콘솔/CLI)

1. **IAM — prod EB 인스턴스 프로파일**에 `CloudWatchAgentServerPolicy` 부착:
   ```bash
   aws iam attach-role-policy \
     --role-name aws-elasticbeanstalk-ec2-role \
     --policy-arn arn:aws:iam::aws:policy/CloudWatchAgentServerPolicy
   ```
   (실제 인스턴스 프로파일 role 이름은 EB 환경 구성에서 확인)

2. **IAM — dev EC2**: 로그 write 권한 role 부착 권장. 역할이 없으면 `.env`의
   `AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY`로 인증(사이드카가 `env_file: .env` 로드).
   최소 권한:
   ```json
   { "Effect": "Allow",
     "Action": ["logs:CreateLogGroup","logs:CreateLogStream","logs:PutLogEvents","cloudwatch:PutMetricData"],
     "Resource": "*" }
   ```

3. **OTel javaagent 처리** (중요): `appstart`(`00-makeFiles.config`)가 `-javaagent:opentelemetry-javaagent.jar`로
   기동하며 traces를 구 Alloy(`localhost:4317`)로 export 했다. Alloy 제거 후 export 대상이 사라져
   재시도 에러 로그가 발생할 수 있다. Blue/Green 신규 env에서 다음 중 하나 적용:
   - EB 환경변수 `OTEL_SDK_DISABLED=true` 설정 (가장 간단), 또는
   - `appstart`에서 `-javaagent` 라인 제거 (OTel 완전 폐지 시)

   > 본 런북은 Sentry(에러 트래킹)는 유지한다. OTel 트레이싱은 Tempo(폐지 대상)로 가던 것이라 비활성 권장.

4. **RDS 지표**: 네이티브 `AWS/RDS` CloudWatch 콘솔에서 조회 (별도 설정 불필요).

### 검증
- CW Logs 그룹 `/ddingdong/prod/application`, `/ddingdong/dev/application` 에 스트림 수신
- CWAgent 네임스페이스에 prod CPU/mem/disk 메트릭 수신
- 앱 부팅 로그에 Alloy/Promtail Runner 관련 로그 없음, OTLP export 에러 없음

---

## Phase A — IPv4 낭비 즉시 정리 (무손실)

> **되돌릴 수 없음.** 실행 전 각 EIP가 정말 미사용인지 재확인.

```bash
# 1) 현재 EIP 전수 조회 — Association/Instance 없는 것이 release 후보
aws ec2 describe-addresses \
  --query 'Addresses[].{IP:PublicIp,Alloc:AllocationId,Assoc:AssociationId,ENI:NetworkInterfaceId,Instance:InstanceId}' \
  --output table

# 2) idle EIP (미연결) release — allocation-id 는 위 조회 결과로 대체
aws ec2 release-address --allocation-id <alloc-id-of-idle-EIP>

# 3) 고아 ENI에 붙은 EIP: 먼저 ENI 상태 확인 → ENI 삭제 → EIP release
aws ec2 describe-network-interfaces --network-interface-ids <eni-id> \
  --query 'NetworkInterfaces[].{Status:Status,Attach:Attachment}'
aws ec2 delete-network-interface --network-interface-id <eni-id>   # Status=available 일 때만
aws ec2 release-address --allocation-id <alloc-id>
```

plan 조사 스냅샷(재확인 필수): idle `15.164.132.178`, 고아 ENI `eni-0489bea8ba470b882`(`43.200.194.17`), `eni-02a1b18f604700f47`(`54.116.150.129`).
남은 `13.124.185.63`(모니터링), `3.37.x`(app-prod)는 Phase D·E 서버 정리와 함께 처리.

**롤백**: EIP release는 복구 불가(같은 IP 재획득 보장 없음). 단 도메인은 CNAME/신규 EIP로 재연결 가능. release 전 해당 IP가 Route53/외부 화이트리스트에 물려있지 않은지 확인.

---

## Phase B — RDS Graviton 전환 (앱 변경 없음)

```bash
aws rds describe-db-instances --db-instance-identifier ddingdong-dev \
  --query 'DBInstances[].{Class:DBInstanceClass,Status:DBInstanceStatus}'

aws rds modify-db-instance \
  --db-instance-identifier ddingdong-dev \
  --db-instance-class db.t4g.micro \
  --apply-immediately
```

- 수정 중 **짧은 재시작** 발생 → 저트래픽 시간대 실행.
- 엔드포인트 DNS 불변 → `${DB_URL}` 앱 설정 무영향.
- **롤백**: 동일 명령으로 `db.t3.micro` 재지정.

---

## Phase C — prod EB Blue/Green (t4g ARM + ALB 제거)

현재 live: `ddingdong-dev-env` (t3.small, ALB 부착).

```bash
# 1) ARM 플랫폼 브랜치 가용성 확인
aws elasticbeanstalk list-available-solution-stacks \
  --query 'SolutionStacks[?contains(@, `Corretto 21`)]' --output text | tr '\t' '\n' | grep -i arm

# 2) 신규 환경 생성: single-instance(ALB 없음) + t4g.small + ARM64 플랫폼
#    (콘솔 또는 create-environment; OptionSettings 로 EnvironmentType=SingleInstance,
#     InstanceType=t4g.small, IamInstanceProfile 지정)
```

핵심 확인 사항:
- JAR은 아키텍처 무관, OTel javaagent도 순수 Java → 소스 그대로 배포 가능.
- `.ebextensions`/`.platform`/`Procfile` 전부 ARM-safe (docker-compose 설치는 `uname -m` 동적).
- **TLS 종료 위치**: 기존 ALB에서 하던 HTTPS 종료를 single-instance 전환 시 어디서 할지 결정 필요.
  옵션 (a) 인스턴스 nginx에 ACM 대신 Let's Encrypt/직접 인증서, (b) CloudFront 앞단 TLS,
  (c) ALB 대신 도메인 → EIP A레코드 + nginx 80만(내부). **실행 시 현행 도메인/인증서 구성 점검 후 확정.**
- 현 nginx는 `:8080`(앱) / `:9090`(actuator) 프록시 (`.platform/nginx/nginx.conf`).

절차: 신규 env에 배포 → 헬스 Green + `/server/actuator/health` 200 + 실제 트래픽 검증 → **CNAME swap** → 기존 env terminate.

**롤백**: CNAME swap은 즉시 역스왑 가능. 구 env는 검증 완료 전까지 terminate 금지.

---

## Phase D — dev EC2 Graviton + 이름 정리 (코드: ✅ 워크플로우 완료)

이 브랜치 반영:
- `dev-server-integrator.yml`: `docker buildx build --platform linux/arm64 --push` (QEMU+Buildx 셋업 추가)
- `dev-server-deployer.yml`: docker-compose 바이너리 `uname -m` 동적 선택(aarch64 대응)
- `docker-compose.yml`(로컬 전용) `platform: linux/x86_64` 는 **그대로 둠** (ARM 배포 경로와 무관)

AWS 작업:
```bash
# 신규 t4g.small 기동 후 EBS/보안그룹 승계 → 기존 dev 인스턴스 교체
# 인스턴스 Name 태그 정리 (실제 역할대로 — 착수 시 사용자 최종 확인)
aws ec2 create-tags --resources <instance-id> --tags Key=Name,Value=<new-name>
```

| 현재 이름 | 실제 역할 | 제안 |
|---|---|---|
| `ddingdong-dev-env` (EB) | 현재 live 서비스 | Phase C 신규 env로 대체 |
| `ddingdong-app-prod` (t2.micro, stopped) | 개발용으로 보임 | `ddingdong-dev-app` |
| `ddingdong-monitoring-prod` (t3.small, stopped) | 모니터링 | Phase E 후 terminate |
| `load-test-server` (t4g.small, stopped) | 부하테스트 | 미사용이면 terminate 검토 |

> ⚠️ dev→prod 이전 상황이라 이름 매핑이 헷갈림. **rename/terminate 전 반드시 실제 역할 재확인.**

---

## 검증 후 — 모니터링 EC2 AMI + terminate

```bash
# 1) AMI 생성 (스냅샷 → 언제든 복원 가능)
aws ec2 create-image --instance-id <monitoring-instance-id> \
  --name "ddingdong-monitoring-backup-$(date +%Y%m%d)" \
  --description "Grafana LGTM monitoring server backup before decommission" --no-reboot

# 2) AMI available 확인 후 terminate
aws ec2 describe-images --image-ids <ami-id> --query 'Images[].State'
aws ec2 terminate-instances --instance-ids <monitoring-instance-id>

# 3) 모니터링 EIP release
aws ec2 release-address --allocation-id <alloc-id-of-13.124.185.63>
```

**롤백**: AMI에서 `run-instances --image-id <ami-id>` 로 즉시 복원.

---

## Phase F — dev EC2 시간제 스케줄링 (compute 절반)

매일 낮 12시 start / 밤 12시 stop (KST, 12h 가동).

권장: **EventBridge Scheduler 2개** (Universal target으로 EC2 Start/StopInstances 직접 호출).

```
start: cron(0 12 * * ? *)  Asia/Seoul  → ec2:StartInstances
stop : cron(0 0  * * ? *)  Asia/Seoul  → ec2:StopInstances
```

- Scheduler 실행 role에 `ec2:StartInstances`/`ec2:StopInstances` 권한.
- dev는 docker-compose EC2 → stop/start 안전(부팅 시 compose 재기동, `restart: unless-stopped`).
- RDS(dev DB 공유)는 상시 유지.
- stop/start 시 public IP 재할당 주의 → EIP 또는 IPv6로 도메인 고정.

**롤백**: 스케줄 disable/삭제 시 상시 가동 복귀.

---

## Phase G — IPv4 → IPv6 전환 (선택, 고노력)

절감 $7.2/월 대비 네트워크 재구성 리스크 큼. **우선순위 낮음.**
- VPC/서브넷 IPv6 CIDR, 라우팅 IPv6 경로, **egress-only IGW**(아웃바운드).
- prod EB·dev EC2 IPv6 할당 + 보안그룹 IPv6 규칙 + Route53 AAAA.
- IPv4-only 엔드포인트(ECR Public, 일부 외부 API, SSH) 통신 경로 필요 → **dualstack 유지가 현실적**(그 경우 IPv4 요금 일부 잔존).
- **판단**: dev 스케줄만으로 목표 달성 → IPv6는 여유 시 별도 진행.

---

## Phase H — 보안 정리 (권장)

- 루트 `.env`에 prod/dev AWS 키 평문 + **root access key**, `.pem` 2개 존재.
- `.env`는 gitignore 확인됨. 그러나 **root 키 → IAM 최소권한 전용 유저 키로 로테이션** 강력 권장.
- 이번 작업에 CloudWatch/EC2/RDS/EB 권한 필요 → 전용 IAM 유저 발급이 자연스러움.
- root access key 발견 시: IAM 콘솔에서 root 액세스 키 **삭제**(AWS 공식 권고: root 키는 존재 자체가 위험).

---

## 최종 검증 체크리스트

- [ ] `aws ce get-cost-and-usage` (전환 1~2일 후): VPC/ELB 항목 급감
- [ ] prod EB 신규 env Green, `/server/actuator/health` 200, CNAME swap 후 도메인 정상
- [ ] 인스턴스 `uname -m` = `aarch64`, 앱 정상 기동, **한글 배너 폰트 렌더링 확인**(fontconfig/noto-cjk arm64)
- [ ] RDS `db.t4g.micro` available, 앱 DB 연결 정상
- [ ] CW Logs prod+dev `application.log` 스트림 수신, CWAgent 서버스펙 수신, `AWS/RDS` 지표 조회
- [ ] Alloy/Promtail 컨테이너 미기동, 앱 부팅 에러 없음, monitoring EC2 terminate 후 서비스 영향 없음
- [ ] 월말 세전 목표 ~$54 접근
