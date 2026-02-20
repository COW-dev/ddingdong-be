# Work Docs 작성 가이드

ddingdong-be 프로젝트의 작업 문서 작성 규칙.

---

## 카테고리 & 경로

| 카테고리 | 경로 | 언제 사용 |
|----------|------|-----------|
| API 변경 | `docs/api/` | 엔드포인트 추가·수정, Swagger 변경 |
| 인프라 | `docs/infra/` | AWS 설정, 배포, Terraform, MCP |
| 기능 | `docs/features/` | 신규 기능, 도메인 로직 추가·수정 |
| 아키텍처 결정 | `docs/decisions/` | 패턴 선택, 기술 결정(ADR) |
| 디버깅 | `docs/debugging/` | 버그 수정, 트러블슈팅 기록 |
| Claude 설정 | `docs/claude/` | 에이전트·훅·스킬·CLAUDE.md 변경 |

---

## 파일명 규칙

```
{YYYY-MM-DD}-{kebab-case-topic}.md
```

예시:
- `2026-02-19-aws-infra-agent-setup.md`
- `2026-02-19-work-docs-skill.md`
- `2026-02-18-fixzone-soft-delete-fix.md`

---

## 문서 템플릿

```markdown
# {작업 제목}

> **날짜**: YYYY-MM-DD
> **카테고리**: {api | infra | features | decisions | debugging | claude}
> **관련 파일**: `path/to/file.java`, `path/to/other.md`

## 작업 요약

{2-4줄로 무엇을 했는지 설명}

## 변경 내용

### {변경 항목 1}
{설명}

### {변경 항목 2}
{설명}

## 결정 사항

- {결정 1}: {이유}
- {결정 2}: {이유}

## 다음 할 일

- [ ] {미완료 항목 1}
- [ ] {미완료 항목 2}
```

---

## 작성 원칙

- **확인된 내용만** — 추측이나 "아마도"는 쓰지 않음
- **간결하게** — 각 섹션은 핵심만, 설명이 불필요하면 섹션 생략 가능
- **파일 경로 명시** — 변경된 파일은 상대 경로로 기재
- **다음 할 일 포함** — 미완료 작업이 있으면 반드시 기록

---

## 섹션 생략 기준

| 섹션 | 생략 조건 |
|------|-----------|
| 변경 내용 | 파일 변경이 없는 순수 개념 설명 작업 |
| 결정 사항 | 단순 버그 수정처럼 선택지가 없었던 경우 |
| 다음 할 일 | 작업이 완전히 완료된 경우 |

---

## 예시 문서

```markdown
# aws-infra 에이전트 설정 및 스킬 생성

> **날짜**: 2026-02-19
> **카테고리**: claude
> **관련 파일**: `.claude/agents/aws-infra.md`, `.claude/skills/work-docs/`

## 작업 요약

AWS 인프라 작업을 위한 `aws-infra` 에이전트 구조를 파악하고,
작업 내용을 docs/에 문서화하는 `work-docs` 스킬을 생성했다.

## 변경 내용

### aws-infra 에이전트 파악
- 위치: `.claude/agents/aws-infra.md`
- AWS CLI / Terraform 중 자동 선택
- 기본 리전: `ap-northeast-2`

### work-docs 스킬 생성
- 위치: `.claude/skills/work-docs/`
- `/document` 또는 "문서화해줘" 로 트리거
- 생성 경로: `docs/{category}/{YYYY-MM-DD}-{topic}.md`

## 결정 사항

- Hook 대신 스킬 선택: 사용자가 명시적으로 요청할 때만 문서 생성하는 것이 적합
- 카테고리별 하위 디렉토리 구조: 프로젝트가 커질수록 탐색 용이

## 다음 할 일

- [ ] AWS CLI 자격증명 설정 (`aws configure`)
```
