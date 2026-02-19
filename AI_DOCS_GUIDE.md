# AI Agent 문서 작성 가이드

> CLAUDE.md, AGENTS.md를 작성하기 전에 이 문서를 먼저 읽고 규칙에 따라 작성한다.

---

## 1. 파일별 역할 구분

| 파일 | 독자 | 역할 |
|------|------|------|
| `CLAUDE.md` | Claude Code (Anthropic) | Claude 전용 지시. 프로젝트 규칙, 명령어, 아키텍처 |
| `AGENTS.md` | 모든 AI 에이전트 (Claude, Gemini, Codex 등) | 범용 에이전트 지침. 여러 도구가 읽는 공통 컨텍스트 |

**관계:** `AGENTS.md`는 공통 기반, `CLAUDE.md`는 Claude 전용 추가 규칙.
중복 내용은 `AGENTS.md`에만 작성하고, `CLAUDE.md`에서 `@AGENTS.md`로 참조한다.

---

## 2. 공통 원칙

### 2-1. 분량
- **`CLAUDE.md`**: 100~200줄 이내 (절대 300줄 초과 금지)
- **`AGENTS.md`**: 150줄 이내
- 초과 시 → 하위 폴더에 별도 파일로 분리하고 링크만 기재

### 2-2. 작성 우선순위 (중요한 순서)
1. **명령어** — 빌드, 테스트, 실행 방법 (없으면 AI가 틀린 명령어를 실행함)
2. **아키텍처/구조** — 어떤 패턴, 어떤 폴더에 무엇이 있는지
3. **금지 사항** — 절대 하면 안 되는 것 (보안, 패턴 위반 등)
4. **컨벤션** — 네이밍, PR 규칙 등

### 2-3. 작성 금지 항목
- ❌ 코딩 스타일(indent, quote 등) → linter/formatter에 위임
- ❌ 코드 스니펫 직접 삽입 → 금방 outdated됨, 파일 경로로 참조
- ❌ 당연한 말 ("코드를 잘 작성하세요")
- ❌ 모든 것에 **IMPORTANT**, CRITICAL 사용 → 강조가 희석됨
- ❌ 추측성 설명 ("아마도 이렇게 동작할 것입니다")

### 2-4. 좋은 표현 vs 나쁜 표현

| 나쁜 예 | 좋은 예 |
|---------|---------|
| "코드를 깔끔하게 작성하세요" | "새 도메인은 `domain/` 하위에 추가하고 DDD 구조를 따른다" |
| "테스트를 잘 작성하세요" | `./gradlew test --tests "*ClassName"` |
| "보안에 신경 쓰세요" | "secrets를 코드에 하드코딩 금지. 환경변수 사용" |
| 긴 설명문 | 짧은 bullet + 코드 예시 경로 참조 |

### 2-5. 파일 참조 문법 (CLAUDE.md 전용)
```
@README.md          # 다른 파일 전체를 임포트
@src/main/java/...  # 특정 파일 참조
```
→ 내용을 복사하지 말고 참조로 연결

---

## 3. CLAUDE.md 작성 규칙

### 필수 섹션 (순서대로)
```
1. 프로젝트 한 줄 설명
2. 핵심 명령어 (빌드, 테스트, 실행)
3. 아키텍처 & 디렉토리 구조
4. 작업 규칙 (AI가 반드시 지켜야 할 것)
5. 절대 하지 말 것
```

### 작성 팁
- 세션마다 Claude가 처음 읽는다고 가정하고 작성
- "왜(Why)"보다 "무엇(What)"과 "어떻게(How)"에 집중
- PR 리뷰에서 반복적으로 지적되는 사항 → CLAUDE.md에 추가
- 새 컨벤션이 생기면 같은 PR에서 CLAUDE.md도 업데이트

### 업데이트 기준
- 빌드/테스트 명령어 변경 시 → 즉시 수정
- 아키텍처 패턴 추가/변경 시 → 즉시 수정
- AI가 같은 실수를 2번 이상 반복 시 → 금지 규칙 추가

---

## 4. AGENTS.md 작성 규칙

### 필수 섹션
```
1. 프로젝트 설명 (1~3줄)
2. 기술 스택 (버전 포함)
3. 설정 & 실행 명령어
4. 테스트 방법
5. 프로젝트 구조 요약
6. 에이전트 행동 규칙
```

### 작성 팁
- 특정 AI 도구 문법 사용 금지 (순수 마크다운만)
- 어떤 AI가 읽어도 이해할 수 있도록 범용적으로 작성
- 빌드/테스트 명령어는 복사-붙여넣기 가능한 형태로

---

## 5. 이 프로젝트(ddingdong-be) 전용 체크리스트

CLAUDE.md / AGENTS.md 작성 후 아래 항목 확인:

### 반드시 포함해야 할 내용
- [ ] Gradle 빌드 명령어 (`-x test` 옵션 포함)
- [ ] 로컬 실행 명령어 (profile 지정)
- [ ] 테스트 실행 명령어 (Docker 필요 명시)
- [ ] DDD 패키지 구조 설명 (domain/api/service/repository)
- [ ] 주요 도메인 목록 (club, form, fixzone 등)
- [ ] Soft Delete 패턴 (`@SQLDelete`, `@SQLRestriction`)
- [ ] API 계층 구조 (Controller → Service → Repository)
- [ ] Facade 패턴 설명
- [ ] 환경변수/secrets 하드코딩 금지 규칙
- [ ] 브랜치 & PR 컨벤션 (한국어 커밋)
- [ ] 새 기능 추가 시 테스트 필수 규칙

### 포함하지 말아야 할 내용
- [ ] application.yml 내용 직접 기재 (secrets 포함 가능성)
- [ ] 코드 스니펫 (checkstyle/spotbugs로 대체)
- [ ] 추측성 설명 또는 미확인 동작 방식

---

## 6. 파일 위치 구조 (권장)

```
ddingdong-be/
├── CLAUDE.md              # 팀 공유 Claude 지침 → 루트에, git 커밋
├── AGENTS.md              # 모든 AI 에이전트 공통 가이드 → 루트에, git 커밋
├── AI_DOCS_GUIDE.md       # 이 파일 (작성 지침)
├── .claude/               # Claude Code 도구 설정 (git 커밋)
│   ├── settings.json      # hooks, permissions 등 도구 설정
│   └── rules/             # (선택) 규칙을 파일별로 분리할 때
│       ├── architecture.md
│       └── security.md
└── CLAUDE.local.md        # 개인 전용 메모 → .gitignore에 추가, 커밋 X
```

### 위치 선택 기준

| 상황 | 파일 위치 |
|------|-----------|
| 팀 전체가 공유해야 하는 규칙 | 루트 `CLAUDE.md` ✅ |
| 나만 쓰는 개인 메모/설정 | `CLAUDE.local.md` (gitignore) |
| 내 모든 프로젝트에 적용할 규칙 | `~/.claude/CLAUDE.md` |
| Claude Code 도구 설정 (hooks 등) | `.claude/settings.json` |
| 규칙이 너무 많아 분리 필요 | `.claude/rules/*.md` |

> **결론**: 이 프로젝트는 팀 공유 목적이므로 루트 `CLAUDE.md`가 정답.

---

## 참고 자료

- [How to Write a Good CLAUDE.md - Builder.io](https://www.builder.io/blog/claude-md-guide)
- [Claude Code Best Practices - Anthropic](https://www.anthropic.com/engineering/claude-code-best-practices)
- [AGENTS.md Official Format](https://agents.md/)
- [Improve your AI code output with AGENTS.md - Builder.io](https://www.builder.io/blog/agents-md)
- [Writing a good CLAUDE.md - HumanLayer](https://www.humanlayer.dev/blog/writing-a-good-claude-md)
