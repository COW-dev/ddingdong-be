# prod SES·SNS·SQS·Lambda 설정 불일치 수정

> **날짜**: 2026-02-20
> **카테고리**: infra
> **관련 리소스**:
> - Lambda: `ddingdong-ses-status-update-prod`
> - SNS: `arn:aws:sns:ap-northeast-2:<ACCOUNT_ID>:ddingdong-email-ses-event-prod`
> - SQS: `https://sqs.ap-northeast-2.amazonaws.com/<ACCOUNT_ID>/ddingdong-ses-queue-prod`

## 작업 요약

dev/prod 인프라 설정을 비교한 결과, prod 환경에서 4개의 불일치/문제점을 발견하고 수정했다.
Lambda 타임아웃 오설정, SNS 보안 정책 취약점, SQS DLQ 설정 불일치, SQS 정책 중복 Statement 정리가 포함된다.

## 변경 내용

### 1. Lambda prod Timeout 수정 (3초 → 120초)

- **문제**: `ddingdong-ses-status-update-prod` Timeout이 3초로 설정되어 SES 이벤트 처리 중 강제 종료 위험
- **수정**: dev와 동일하게 120초로 변경

```bash
aws lambda update-function-configuration \
  --function-name ddingdong-ses-status-update-prod \
  --timeout 120
```

### 2. SNS prod 토픽 Policy 보안 강화

- **문제**: prod SNS 토픽 Policy가 모든 계정(`Principal: *`)에 Publish 허용 — 보안 취약
- **수정**: dev와 동일하게 `ses.amazonaws.com`만 Publish 허용하도록 제한

**수정 전 (취약):**
```json
"Principal": {"AWS": "*"},
"Action": ["SNS:Publish", "SNS:RemovePermission", ...]
```

**수정 후 (보안):**
```json
"Principal": {"Service": "ses.amazonaws.com"},
"Action": "SNS:Publish",
"Condition": {
  "StringEquals": {"AWS:SourceAccount": "<ACCOUNT_ID>"},
  "StringLike": {"AWS:SourceArn": "arn:aws:ses:*"}
}
```

### 3. SQS prod DLQ maxReceiveCount 통일 (10 → 3)

- **문제**: dev는 3회 실패 시 DLQ 이동, prod는 10회로 불일치
- **수정**: dev와 동일하게 `maxReceiveCount: 3`으로 변경

```bash
aws sqs set-queue-attributes \
  --queue-url .../ddingdong-ses-queue-prod \
  --attributes '{"RedrivePolicy":"{...\"maxReceiveCount\":3}"}'
```

### 4. SQS prod Policy 중복 Statement 제거

- **문제**: SNS SendMessage를 허용하는 Statement가 2개 중복 존재 (`Allow-SNS-SendMessage`, `topic-subscription-...`)
- **수정**: 중복 Statement 제거, `topic-subscription` 하나로 통일

## 결정 사항

- Lambda Timeout은 dev 기준(120초)을 prod에도 동일 적용: SES 이벤트는 외부 API 호출 포함으로 처리 시간 여유 필요
- SNS Policy는 최소 권한 원칙 적용: SES 이외의 소스가 Publish할 이유 없음
- DLQ maxReceiveCount는 dev 기준(3회)으로 통일: 빠른 실패 감지가 운영에 유리

## 최종 설정 현황 (dev = prod 기준)

| 항목 | dev | prod (수정 후) |
|------|-----|----------------|
| Lambda Timeout | 120초 | 120초 ✅ |
| SNS Publish 허용 | SES only | SES only ✅ |
| SQS DLQ maxReceiveCount | 3 | 3 ✅ |
| SQS Policy Statement 수 | 2 | 2 ✅ |
