# 인수인계: 개발(staging) 서버 dev계정 → prod계정 이전

작성 2026-07-10 · 담당 KoSeonJe · 상태 **완료(정리단계만 남음)**

---

## 0. 한 줄 요약
dev AWS 계정(014498661197)에 있던 **staging 앱 서버 + 모니터링 서버**를 prod 계정(531444319321)으로 이전하고, 코드/CI/DNS의 dev 계정 의존성을 제거했다. staging.api-ddingdong.net은 이미 새 서버로 전환됨.

---

## 1. 이전 대상 & 결과 (Before → After)

| 자원 | Before (dev 계정) | After (prod 계정) |
|------|-------------------|-------------------|
| staging 앱 EC2 | 43.200.27.15 (t2.micro) | **3.37.32.225** (i-068236db10f10c0e8, t2.micro) |
| 모니터링 EC2 | 3.39.151.102 (t2.micro) | **13.124.185.63** (i-0ffe9d5b3d8b8d961, **t3.small**) |
| 트레이스 S3 | observability-ddingdong | **observability-ddingdong-prod** |
| 컨테이너 이미지 | public.ecr.aws/r6c6y8k1/dev-ecr | **public.ecr.aws/a7c1z1y2/dev-ecr** |
| staging DNS | dev Route53 위임존 | **prod api-ddingdong.net 존 직접 A레코드** |

> 앱 파일 S3(ddingdong-file 등)·SES는 원래부터 prod 계정 → 변경 없음.

---

## 2. 새 인프라 구성 (prod 계정, ap-northeast-2)

**공통**: VPC `vpc-04c30100bc34baddf`(default, 172.31), subnet `subnet-05f3e86bdea7a70b8`(AZ-2a), keypair `ddingdong-dev-migrated`(기존 dev SSH키와 동일 공개키 → 같은 pem 사용)

### staging 앱 서버 (3.37.32.225)
- Ubuntu 22.04, nginx(80/443, Let's Encrypt certbot) → spring 컨테이너(8080) + mysql:8.4.6 컨테이너
- 이미지: `public.ecr.aws/a7c1z1y2/dev-ecr:latest` (prod ECR)
- DB `ddingdong` (28테이블), docker 외부 볼륨 `mysql-volume`
- SG `sg-0bbe8e1cfc130cb9b`: 80/443/8080 공개, **22는 관리자 IP만** (3306 비공개 ← 기존 dev는 전세계 오픈이었음)
- **docker-compose v2.29.7** (v1은 새 엔진서 크래시 → 교체)

### 모니터링 서버 (13.124.185.63)
- Grafana(3000) / Prometheus(9090) / Loki(3100) / Tempo(3200, S3백엔드) / OTLP(4317/4318)
- IAM instance profile `tempo-s3-access-role` (S3 **버킷 한정** 정책)
- SG `sg-03447143d96628d74`: **push포트(9090/3100/4317/4318)=VPC내부(172.31.0.0/16)**, 관리포트(3000/3200/22)=관리자 IP
- 디스크 50GB, docker 로그로테이션 적용

### DNS (Route53, prod)
- 공개 `monitoring.api-ddingdong.net` → 13.124.185.63 (브라우저/Grafana 접속용)
- **프라이빗 존** `monitoring.api-ddingdong.net`(VPC 연결) → 172.31.15.39 (사설IP)
  - VPC 내부 앱(EB/staging)은 사설IP로 접속 → 모니터링 수집포트를 외부에 안 열어도 됨
- `staging.api-ddingdong.net` → **3.37.32.225** (컷오버 완료)

---

## 3. 코드 변경 (PR #420, → develop)
브랜치 `feat/monitoring-endpoint-domain`
- `alloy/config.alloy`: Prometheus remote-write / OTLP 엔드포인트 옛IP → `monitoring.api-ddingdong.net`
- `promtail/promtail-docker-compose.yml`: Loki push URL 도메인화
- `.github/workflows/dev-server-deployer.yml`: docker-compose v1.29.2 → **v2.29.7**

> ⚠️ **PR #420 머지 + 운영 EB 재배포 필요**. 안 하면 운영 EB 앱의 메트릭/로그/트레이스가 옛 모니터링 IP로 가서, dev 모니터링 종료 시 수집 끊김.

---

## 4. GitHub Secrets 변경 (COW-dev/ddingdong-be)
| Secret | 값 |
|--------|----|
| `DEV_ECR_REGISTRY_ALIAS` | a7c1z1y2 |
| `DEV_EC2_SECURITY_GROUP_ID` | sg-0bbe8e1cfc130cb9b |
| `DEV_INSTANCE_HOST` | 3.37.32.225 |
| `DEV_AWS_ACCESS_KEY_ID` / `_SECRET` | prod IAM 유저 `ddingdong-github-actions` 키 |

> `SERVER_URL`(운영 EB용), `DEV_INSTANCE_KEY`는 미변경.

---

## 5. 배포 방법 (동료용 런북)
- **staging 배포**: GitHub Actions → `Develop Server Deployer` 수동 실행(workflow_dispatch). prod ECR 이미지 pull → compose v2 up.
- **staging CI**: develop push 시 `Develop Server Integrator` → prod ECR로 이미지 push.
- **운영 배포**: main push → `prod-server-deployer` → Elastic Beanstalk `ddingdong-dev-env` (이번 이전과 별개).
- **SSH**: `ssh -i ddingdong-server-dev.pem ubuntu@3.37.32.225` (22는 관리자 IP만 → 접속 안 되면 SG에 본인 IP 추가 필요)
- **Grafana**: http://monitoring.api-ddingdong.net:3000 (SG에 본인 IP 등록 필요)

---

## 6. 이전 중 잡은 이슈 (참고)
1. 모니터링 서버 **디스크 100% → impaired(다운)**: 컨테이너 로그 21GB 무제한 누적이 원인 → 로그로테이션 적용
2. loki 퍼미션 버그(`/loki` 쓰기불가 크래시) → 볼륨+root 실행으로 수정
3. docker-compose v1이 새 엔진서 `ContainerConfig` 크래시 → v2 전환
4. .env의 멀티라인 RSA키가 compose v2 파싱 차단 → 제거(앱 런타임 불필요)
5. staging 박스 호스트 mysqld가 3306 선점(컨테이너 충돌) → 비활성화

---

## 7. ⏳ 남은 작업

### A. PR #420 머지 + 운영 EB 재배포 (우선)
모니터링 도메인 반영. dev 모니터링 종료 **전에** 필수.

### B. dev 자원 정리 (정상 관찰 후 — 며칠 뒤)
```
# dev 계정
- EC2 종료: i-057bcd92251f523ea, i-015507849d3e5a648 (stop→terminate)
- EIP release: 43.200.27.15, 3.39.151.102
- S3 삭제: observability-ddingdong
- Route53 존 삭제: staging.api-ddingdong.net (Z07845103ZNPJEEM187N)
- ECR 삭제: r6c6y8k1/dev-ecr
- 마이그레이션 AMI/스냅샷 정리 (양 계정)
```

### C. 보안 (별건, 권장)
- **root 액세스 키 폐기**: `.env`의 dev/prod 키가 둘 다 **root 키**. IAM 유저로 교체 후 root 키 삭제.
- staging 박스 SG 관리포트 CIDR을 `/24` → `/32`로 축소
- staging 박스 t2.micro는 메모리 빠듯(swap 사용중) → 필요시 t3.small 상향 검토

---

## 8. 롤백 (문제 시)
- staging: prod 부모존 A레코드 `staging.api-ddingdong.net` → 43.200.27.15 로 되돌리고 NS위임 복구 (옛 dev 서버 아직 살아있음)
- 로컬 백업: `~/dev/ddingdong-dev-backup-20260709-233948/` (DB덤프·config·S3)

---
## 관련 문서
- 상세 변경내역: `CHANGES-APPLIED.md`
- 사이드이펙트 분석: `migration-side-effects-plan.md`
- 초기 이전 계획: `dev-to-prod-migration-plan.md`
