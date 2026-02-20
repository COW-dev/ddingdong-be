---
name: work-docs
description: |
  This skill should be used when the user asks to document their work,
  requests a summary of the conversation as a doc, or uses commands like
  "문서화해줘", "작업 내용 정리해줘", "/document", "/docs".
  Creates structured Markdown files under docs/ reflecting completed work.
---

# Work Docs

대화에서 수행한 작업을 `docs/` 하위에 구조화된 마크다운 문서로 저장.

## When to Use

- "문서화해줘", "작업 내용 정리해줘", "docs 만들어줘" 요청 시
- "/document", "/docs" 슬래시 커맨드 입력 시
- 작업 묶음이 끝난 뒤 기록을 남기고 싶을 때

## How to Use

Load `references/docs-guide.md` for document structure, naming conventions,
and category rules before writing any file.

## Process

1. **대화 분석**: 이번 대화에서 수행한 작업을 파악
   - 어떤 문제를 해결했는가?
   - 어떤 파일이 변경되었는가?
   - 어떤 결정이 내려졌는가?

2. **카테고리 결정**: `references/docs-guide.md`의 카테고리 규칙 참고
   - `api/` — API 설계·변경
   - `infra/` — AWS·배포·인프라 설정
   - `features/` — 기능 추가·수정
   - `decisions/` — 아키텍처 결정(ADR)
   - `debugging/` — 버그 수정·디버깅
   - `claude/` — 에이전트·훅·스킬·CLAUDE.md 변경

3. **파일명 결정**: `{YYYY-MM-DD}-{kebab-case-topic}.md`
   - 예: `2026-02-19-aws-infra-agent-setup.md`

4. **문서 작성**: `references/docs-guide.md`의 템플릿 사용
   - 핵심만 간결하게 (추측 금지, 확인된 내용만)

5. **파일 저장**: `docs/{category}/{파일명}` 경로에 Write 도구로 저장
   - `docs/` 디렉토리가 없으면 생성

6. **완료 보고**: 저장된 파일 경로를 사용자에게 알림
