# Claude 에이전트·스킬 설정

> **날짜**: 2026-02-19
> **카테고리**: claude
> **관련 파일**: `.claude/agents/aws-infra.md`, `.claude/skills/work-docs/`, `.gitignore`

## 작업 요약

AWS 인프라 작업용 `aws-infra` 에이전트의 동작 방식을 파악하고,
작업 내용을 `docs/`에 문서화하는 `work-docs` 스킬을 신규 생성했다.
기존에 있던 `agent-creator`, `hook-creator`, `skill-creator` 스킬도 함께 커밋했다.

## 변경 내용

### aws-infra 에이전트 (`aws-infra.md`)
- AWS CLI / Terraform 작업 전용 에이전트
- **트리거 조건**: "S3 버킷 만들어줘", "EC2 목록 보여줘" 등 AWS 관련 요청 시 Claude가 자동 판단
- 파일 변경·저장 이벤트로 자동 실행되지 않음 (대화 기반 트리거)
- 기본 리전: `ap-northeast-2`
- AWS CLI 자격증명 필요 (`aws configure` 또는 `~/.aws/credentials`)

### work-docs 스킬 (신규)
- 위치: `.claude/skills/work-docs/`
- **트리거**: "문서화해줘", "작업 내용 정리해줘", `/document`
- 생성 경로: `docs/{category}/{YYYY-MM-DD}-{topic}.md`
- 카테고리: `api`, `infra`, `features`, `decisions`, `debugging`, `claude`

### .gitignore 수정
- `.claude/settings.local.json` 추가 — 로컬 권한·훅 설정은 개인 파일이므로 제외

## 결정 사항

- **Hook 대신 Skill 선택**: 자동 생성보다 명시적 요청 시 문서화가 품질 면에서 적합
- **카테고리별 디렉토리 구조**: 프로젝트 규모가 커질 때 탐색 용이

## 다음 할 일

- [ ] AWS CLI 자격증명 설정 (`aws configure`) — aws-infra 에이전트 사용 시 필요
