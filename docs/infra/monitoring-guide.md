# 모니터링 가이드 — 로그 & 메트릭 보는 법 (팀 브리핑)

> 자체 호스팅 모니터링(Grafana + Loki + Prometheus + Alloy + Promtail)을 폐지하고
> **AWS CloudWatch**로 이전했습니다. 이제 로그·서버 지표는 전부 CloudWatch에서 봅니다.
> 리전: **서울 (ap-northeast-2)**

---

## 요약: 무엇을 어디서 보나

| 보고 싶은 것 | 어디서 | 위치 |
|--------------|--------|------|
| **애플리케이션 로그** (prod) | CloudWatch Logs | 로그 그룹 `/ddingdong/prod/application` |
| **애플리케이션 로그** (dev) | CloudWatch Logs | 로그 그룹 `/ddingdong/dev/application` |
| **서버 지표** (CPU/메모리/디스크) — prod | CloudWatch Metrics | 네임스페이스 `CWAgent` |
| **DB 지표** (RDS) | CloudWatch Metrics | 네임스페이스 `AWS/RDS` |
| **에러 트래킹** | Sentry | 기존 Sentry 대시보드 (변경 없음) |

> 기존 Grafana/Prometheus 주소(`monitoring.api-ddingdong.net` 등)는 **더 이상 사용하지 않습니다.**

---

## 1. 애플리케이션 로그 보기

앱이 남기는 로그(`application.log`)를 CloudWatch Agent가 실시간으로 CloudWatch Logs에 전송합니다.

### 콘솔에서 보기
1. AWS 콘솔 → **CloudWatch** → 왼쪽 메뉴 **Logs → Log groups**
2. 로그 그룹 선택
   - 운영: **`/ddingdong/prod/application`**
   - 개발: **`/ddingdong/dev/application`**
3. 로그 스트림 클릭 (스트림 이름 = 인스턴스 ID / 호스트명)
4. 실시간으로 보려면 우측 상단 **"Live tail"** 또는 검색

> **Logs Insights** 로 여러 스트림을 한 번에 쿼리할 수 있습니다:
> CloudWatch → Logs → **Logs Insights** → 로그 그룹 선택 → 예시 쿼리
> ```
> fields @timestamp, @message
> | filter @message like /ERROR/
> | sort @timestamp desc
> | limit 100
> ```

### CLI로 보기 (빠른 확인)
```bash
# 최근 10분 로그를 실시간으로
aws logs tail /ddingdong/prod/application --since 10m --follow --region ap-northeast-2

# dev
aws logs tail /ddingdong/dev/application --since 10m --region ap-northeast-2
```

### 보관 기간(Retention)
- prod: **30일**
- dev: **14일**
- 기간이 지난 로그는 자동 삭제되어 저장 비용이 무한정 늘지 않습니다.

---

## 2. 서버 지표(메트릭) 보기

### prod 서버 — CPU / 메모리 / 디스크 / 프로세스
CloudWatch Agent가 60초 간격으로 수집해 **`CWAgent`** 네임스페이스로 보냅니다.

1. AWS 콘솔 → **CloudWatch** → **Metrics → All metrics**
2. 네임스페이스 **`CWAgent`** 선택
3. `InstanceId` 기준으로 아래 지표 확인
   - **CPU**: `cpu_usage_idle`, `cpu_usage_user`, `cpu_usage_system`
   - **메모리**: `mem_used_percent`, `mem_available`, `mem_used`
   - **디스크**: `used_percent`, `used`, `total` (루트 `/`)
   - **스왑 / 프로세스**: `swap_used_percent`, `processes_*`

### 기본 EC2 지표 (에이전트 없이도 나오는 것)
- 네임스페이스 **`AWS/EC2`** 에서 `CPUUtilization`, 네트워크, 디스크 I/O 등을 인스턴스별로 확인 가능
- dev 서버는 현재 **로그만** CloudWatch로 보냅니다(서버 스펙 지표는 미수집).
  → dev의 CPU 등은 `AWS/EC2` 기본 지표로 확인하고, 메모리/디스크까지 필요하면 dev 에이전트 설정에 metrics 블록을 추가하면 됩니다.

---

## 3. DB(RDS) 지표 보기

RDS는 AWS가 기본 제공하는 네이티브 지표를 그대로 사용합니다.

- **RDS 콘솔** → 해당 DB 인스턴스 → **Monitoring** 탭, 또는
- **CloudWatch** → Metrics → 네임스페이스 **`AWS/RDS`**
- 주요 지표: `CPUUtilization`, `FreeableMemory`, `DatabaseConnections`, `FreeStorageSpace`, `ReadIOPS/WriteIOPS`

---

## 4. 에러 트래킹 — Sentry (변경 없음)

- 애플리케이션 예외/에러는 기존 **Sentry**에서 계속 봅니다.
- CloudWatch(로그·지표)와 역할이 다릅니다:
  - **Sentry** = 에러 발생·스택트레이스·알림
  - **CloudWatch Logs** = 전체 요청/동작 로그
  - **CloudWatch Metrics** = 서버·DB 리소스 상태

---

## 구성 개요 (어떻게 수집되나)

```
[ prod EB 인스턴스 ]
   앱 → application.log ──┐
                          ├─(CloudWatch Agent, .ebextensions로 설치)→ CloudWatch Logs (/ddingdong/prod/application)
   서버 스펙(CPU/메모리/디스크) ┘                                     → CloudWatch Metrics (CWAgent)

[ dev EC2 (docker-compose) ]
   앱 → /logs/application.log ──(cloudwatch-agent 사이드카 컨테이너)→ CloudWatch Logs (/ddingdong/dev/application)

[ RDS ] ──(AWS 네이티브)→ CloudWatch Metrics (AWS/RDS)
[ 앱 예외 ] ──→ Sentry
```

> **참고**: JVM 커스텀 메트릭(힙·GC 등)은 비용 절감을 위해 CloudWatch로 push하지 않습니다.
> 필요 시 서버에서 Spring Actuator 엔드포인트로 직접 확인합니다.

---

## 트러블슈팅

- **dev 로그가 안 보인다**: dev 서버는 밤 12시~낮 12시에 꺼져 있습니다(스케줄). 그 시간대엔 새 로그가 없습니다.
- **로그 그룹이 비어 있다**: CloudWatch Agent가 인증에 사용하는 자격증명에 로그 쓰기 권한(`CloudWatchAgentServerPolicy`)이 있어야 합니다.
- **CloudWatch 접근 권한이 없다**: IAM에서 `CloudWatchReadOnlyAccess` 정도를 팀 계정에 부여하면 조회 가능합니다.
