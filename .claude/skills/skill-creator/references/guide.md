# Skill Creator: 베스트 프랙티스 가이드

Claude Code 스킬을 만들기 위한 완전한 레퍼런스.

---

## 목차

1. [스킬이란?](#스킬이란)
2. [디렉토리 구조](#디렉토리-구조)
3. [SKILL.md 작성법](#skillmd-작성법)
4. [번들 리소스 종류](#번들-리소스-종류)
5. [Progressive Disclosure 원칙](#progressive-disclosure-원칙)
6. [스킬 생성 프로세스 6단계](#스킬-생성-프로세스-6단계)
7. [완성된 예제](#완성된-예제)
8. [안티패턴](#안티패턴)
9. [검증 체크리스트](#검증-체크리스트)

---

## 스킬이란?

스킬은 Claude에게 특정 도메인 전문 지식, 워크플로우, 도구 통합을 제공하는 모듈형 패키지.
에이전트가 "누가 작업할 것인가"를 정의한다면, 스킬은 "어떻게 작업할 것인가"를 정의.

**스킬이 제공하는 것:**
- **전문 워크플로우** — 특정 도메인의 멀티스텝 절차
- **도구 통합** — 특정 API/포맷 작업 방법
- **도메인 지식** — 프로젝트 고유 스키마, 정책, 비즈니스 로직
- **번들 리소스** — 반복 작업용 스크립트, 레퍼런스, 에셋

---

## 디렉토리 구조

```
.claude/skills/
└── my-skill/
    ├── SKILL.md              ← 필수: 메타데이터 + 지침
    ├── scripts/              ← 선택: 실행 가능 코드
    │   └── rotate_pdf.py
    ├── references/           ← 선택: 문서 (컨텍스트에 로드)
    │   ├── schema.md
    │   └── api-docs.md
    └── assets/               ← 선택: 출력에 사용될 파일
        ├── template.html
        └── logo.png
```

---

## SKILL.md 작성법

### 기본 구조

```markdown
---
name: skill-name
description: |
  This skill should be used when [구체적 사용 조건].
  Provides [무엇을 제공하는지] for [어떤 작업].
license: MIT
---

# Skill Name

[스킬 목적 2-3문장]

## When to Use

[언제 사용해야 하는지]

## How to Use

[Claude가 이 스킬을 어떻게 활용하는지]

### Available Resources

- `scripts/example.py`: [스크립트 설명]
- `references/schema.md`: [레퍼런스 설명]
- `assets/template.html`: [에셋 설명]

## Process

1. [첫 번째 단계]
2. [두 번째 단계]
3. ...
```

### 작성 스타일

**명령형/부정사 형태 사용 (2인칭 금지):**

```
✅ To accomplish X, do Y
✅ Use the Read tool to load the schema
✅ Check references/schema.md for table definitions

❌ You should do X
❌ You will use the Read tool
❌ If you need X, look at Y
```

**description 필드 품질이 핵심:**
- 3인칭으로 작성 ("This skill should be used when...")
- 구체적인 트리거 조건 명시
- 스킬이 제공하는 가치 명확히

---

## 번들 리소스 종류

### scripts/ — 실행 코드

```
언제: 같은 코드가 반복 작성되거나 결정론적 신뢰성 필요
예: scripts/rotate_pdf.py, scripts/parse_data.sh
장점: 토큰 효율적, 결정론적, 컨텍스트 없이 실행 가능
```

**예시 참조 (SKILL.md 내):**
```markdown
To rotate a PDF, execute `scripts/rotate_pdf.py`:
```bash
python scripts/rotate_pdf.py input.pdf --degrees 90
```

### references/ — 문서 (컨텍스트 로드용)

```
언제: Claude가 작업 중 참고해야 하는 문서
예: references/schema.md, references/api-docs.md, references/policies.md
장점: SKILL.md를 간결하게 유지, 필요 시에만 로드
주의: 10k words 초과 시 SKILL.md에 grep 패턴 포함
```

**예시 참조 (SKILL.md 내):**
```markdown
Load `references/schema.md` to understand the database structure
before writing any queries.
```

### assets/ — 출력용 파일

```
언제: 최종 출력에 사용될 파일 (복사/수정 대상)
예: assets/template.html, assets/logo.png, assets/hello-world/
장점: 출력 리소스와 문서 분리
```

**예시 참조 (SKILL.md 내):**
```markdown
Copy `assets/frontend-template/` as the starting point for the new project.
```

### 중복 방지 원칙

```
SKILL.md ← 핵심 절차, 워크플로우 지침
references/ ← 상세 정보, 스키마, 예제, API 문서

같은 정보가 두 곳에 있으면 안 됨.
상세 정보는 references/로, SKILL.md는 린(lean)하게.
```

---

## Progressive Disclosure 원칙

스킬은 3단계 로딩으로 컨텍스트를 효율적으로 관리:

```
1단계: 메타데이터 (name + description)
   → 항상 컨텍스트에 존재 (~100 words)

2단계: SKILL.md 본문
   → 스킬 트리거 시 로드 (< 5,000 words 권장)

3단계: 번들 리소스
   → Claude가 필요하다고 판단할 때 로드 (무제한*)
```

*scripts는 컨텍스트 없이 실행 가능하므로 사실상 무제한

**실전 적용:**
- SKILL.md는 절차와 지침만 → 5,000 words 이하 유지
- 상세 내용은 references/로 분리
- 대용량 references는 SKILL.md에 grep 패턴 힌트 포함

---

## 스킬 생성 프로세스 6단계

### Step 1: 구체적 사용 예제 파악

스킬이 어떻게 사용될지 구체적 예제 수집:

```
질문 예시:
- "이 스킬이 지원해야 하는 기능은 무엇인가요?"
- "어떤 식으로 사용될지 예제를 보여주실 수 있나요?"
- "'이미지 빨간 눈 제거' 같은 요청이 있을까요?"
- "이 스킬을 트리거할 사용자 입력은 무엇인가요?"

완료 조건: 스킬이 지원해야 할 기능이 명확해질 때
```

### Step 2: 재사용 가능한 리소스 계획

각 사용 예제를 분석해 필요한 리소스 도출:

```
분석 프레임:
1. 이 작업을 처음부터 어떻게 실행하는가?
2. 반복할 때마다 다시 작성하게 될 코드/문서는 무엇인가?

예시:
- PDF 회전 → scripts/rotate_pdf.py 필요
- 프론트엔드 빌드 → assets/hello-world/ 보일러플레이트 필요
- DB 쿼리 → references/schema.md 필요
```

### Step 3: 스킬 초기화

```bash
# 스킬 디렉토리 생성
mkdir -p .claude/skills/my-skill/{scripts,references,assets}

# SKILL.md 템플릿 생성
cat > .claude/skills/my-skill/SKILL.md << 'EOF'
---
name: my-skill
description: |
  This skill should be used when...
---

# My Skill

## When to Use

## How to Use

## Process

EOF
```

### Step 4: 스킬 편집

```
우선순위:
1. scripts/, references/, assets/ 리소스 먼저 작성
2. 불필요한 예제 파일 삭제
3. SKILL.md 업데이트 (리소스 참조 포함)

SKILL.md 작성 시 답해야 할 질문:
1. 스킬의 목적은 무엇인가? (2-3문장)
2. 언제 사용해야 하는가?
3. 실제로 Claude는 어떻게 사용하는가?
   (모든 리소스를 참조하도록 지침 포함)
```

### Step 5: 검증 체크리스트

스킬 완성 전 아래 항목을 수동으로 확인한다:

- [ ] `SKILL.md` 프론트매터에 `name`, `description` 필드 존재
- [ ] `description`에 `<example>` 블록 최소 1개 포함
- [ ] 참조 파일이 있다면 `references/` 하위에 위치
- [ ] 슬래시 커맨드명이 소문자·하이픈만 사용 (`my-skill` 형식)
- [ ] Claude Code에서 `/my-skill` 입력 시 정상 트리거 확인

**검증 항목:**
- YAML 프론트매터 형식 및 필수 필드
- 네이밍 컨벤션 및 디렉토리 구조
- description 완성도
- 파일 구성 및 리소스 참조

### Step 6: 반복 개선

```
반복 워크플로우:
1. 실제 작업에 스킬 사용
2. 어려움이나 비효율 발견
3. SKILL.md 또는 번들 리소스 개선
4. 재테스트
```

---

## 완성된 예제

### Kotlin/Spring API 문서화 스킬

```markdown
---
name: spring-api-docs
description: |
  This skill should be used when generating or updating API documentation
  for Kotlin Spring Boot projects. Use when the user asks to document
  REST endpoints, create Swagger annotations, or write API reference docs.
---

# Spring API Docs

Automate API documentation generation for Kotlin Spring Boot projects
using SpringDoc/Swagger annotations and structured Markdown references.

## When to Use

- Documenting new REST controller endpoints
- Adding or updating Swagger/OpenAPI annotations
- Generating API reference documentation

## How to Use

1. Load `references/spring-annotation-guide.md` for annotation patterns
2. Read the target controller file
3. Apply annotations following project conventions
4. Generate Markdown API docs if requested

## Process

1. **Identify scope**: Determine which controllers/endpoints need documentation
2. **Load context**: Read `references/spring-annotation-guide.md`
3. **Analyze controller**: Read existing code structure and method signatures
4. **Apply annotations**: Add SpringDoc annotations following the guide
5. **Generate docs**: Create or update Markdown API reference
6. **Verify**: Ensure all endpoints are covered and examples are accurate
```

```markdown
<!-- references/spring-annotation-guide.md -->
# Spring Annotation Guide

## Controller 레벨
@Tag(name = "User", description = "사용자 관련 API")

## Method 레벨
@Operation(summary = "사용자 조회", description = "ID로 사용자 정보 조회")
@ApiResponses({
  @ApiResponse(responseCode = "200", description = "조회 성공"),
  @ApiResponse(responseCode = "404", description = "사용자 없음")
})
```

---

## 안티패턴

### ❌ SKILL.md에 모든 정보 몰아넣기

```markdown
# 나쁜 예: SKILL.md가 15,000 words
# 모든 스키마, 예제, API 문서가 다 들어있음
```

```markdown
# 좋은 예: SKILL.md는 절차만, 상세 내용은 references/로
Load `references/schema.md` for complete table definitions.
```

### ❌ 2인칭으로 작성

```markdown
# 나쁜 예
You should read the schema file before querying.
If you need help, you can check the references.

# 좋은 예
To query the database, first load `references/schema.md`.
Check the references directory for additional context as needed.
```

### ❌ 모호한 description

```markdown
# 나쁜 예
description: This skill helps with database stuff.

# 좋은 예
description: |
  This skill should be used when writing SQL queries against the
  production analytics database. Provides table schemas, query
  patterns, and optimization guidelines.
```

### ❌ 사용 안 하는 리소스 방치

```
# 나쁜 예: 초기화 스크립트가 만든 예제 파일들을 그냥 둠
scripts/example.py   ← 사용 안 함
assets/sample.png    ← 사용 안 함

# 좋은 예: 불필요한 파일 삭제
```

### ❌ SKILL.md와 references 중복

```
# 나쁜 예: 스키마 정보가 SKILL.md에도, schema.md에도 있음

# 좋은 예: 스키마는 references/schema.md에만
# SKILL.md에는 "Load references/schema.md for schema info"만 기재
```

---

## 검증 체크리스트

```
SKILL.md
□ YAML 프론트매터에 name, description 있음
□ description이 "This skill should be used when..."으로 시작
□ 명령형/부정사 형태로 작성 (2인칭 없음)
□ 5,000 words 이하 (린하게 유지)
□ 모든 번들 리소스가 SKILL.md에서 참조됨

디렉토리 구조
□ 필요한 리소스만 있음 (초기화 예제 파일 정리)
□ scripts/: 실행 가능 여부 (chmod +x)
□ references/: 마크다운 형식, 검색 가능한 구조
□ assets/: 출력에 실제 사용될 파일만

내용 품질
□ SKILL.md와 references 간 중복 없음
□ 스크립트 사용 방법 명확히 문서화
□ 대용량 references (> 10k words) 시 grep 힌트 포함
□ 실제 작업 예제로 테스트 완료

배포
□ 패키징 검증 통과
□ 팀 공유 시 .gitignore에 민감 파일 제외
```

---

## 빠른 레퍼런스

### 스킬 vs 에이전트 선택

| 상황 | 선택 |
|------|------|
| 특정 절차/도메인 지식 주입 | **스킬** |
| 독립적인 전문 작업자가 필요 | **에이전트** |
| 반복되는 코드/문서 재사용 | **스킬** (scripts/assets) |
| 복잡한 멀티스텝 자율 작업 | **에이전트** |
| API 스키마, DB 구조 참조 | **스킬** (references) |

### 파일 위치

| 경로 | 범위 |
|------|------|
| `~/.claude/skills/` | 전역 스킬 |
| `.claude/skills/` | 프로젝트 스킬 |
