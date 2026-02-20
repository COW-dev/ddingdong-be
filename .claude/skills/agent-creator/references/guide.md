# Agent Creator: 베스트 프랙티스 가이드

Claude Code에서 고품질 에이전트를 만들기 위한 완전한 레퍼런스.

---

## 목차

1. [에이전트란?](#에이전트란)
2. [파일 구조](#파일-구조)
3. [프론트매터 레퍼런스](#프론트매터-레퍼런스)
4. [시스템 프롬프트 설계](#시스템-프롬프트-설계)
5. [트리거 예제 작성법](#트리거-예제-작성법)
6. [에이전트 유형별 패턴](#에이전트-유형별-패턴)
7. [완성된 예제](#완성된-예제)
8. [안티패턴](#안티패턴)
9. [빠른 생성 프롬프트](#빠른-생성-프롬프트)

---

## 에이전트란?

에이전트는 특정 도메인 전문 지식을 가진 서브 Claude 인스턴스.
**언제 사용:** 메인 대화 컨텍스트를 보호하거나, 독립적인 전문 작업이 필요할 때.

---

## 파일 구조

```text
.claude/
└── agents/
    └── my-agent.md       ← 에이전트 파일 위치
```

프로젝트별 에이전트는 프로젝트 루트의 `.claude/agents/`에,
전역 에이전트는 `~/.claude/agents/`에 저장.

---

## 프론트매터 레퍼런스

```markdown
---
name: agent-identifier          # 소문자, 숫자, 하이픈만 사용 (필수)
description: |                  # 언제 사용할지 + <example> 블록 (필수)
  Use this agent when...

  <example>
  Context: ...
  user: "..."
  assistant: "..."
  <commentary>...</commentary>
  assistant: "I'll use the agent-name agent to..."
  </example>

model: inherit                  # inherit | claude-opus-4-6 | claude-sonnet-4-6 | claude-haiku-4-5
color: blue                     # blue | cyan | green | yellow | magenta | red
tools:                          # 생략 시 모든 도구 사용 가능
  - Read
  - Grep
  - Glob
---
```

### 컬러 가이드

| 색상 | 용도 |
|------|------|
| `blue` | 분석, 리뷰, 조사 |
| `cyan` | 문서화, 정보 제공 |
| `green` | 생성, 코드 작성, 성공 지향 |
| `yellow` | 검증, 경고, 주의 필요 |
| `red` | 보안, 치명적 분석 |
| `magenta` | 리팩토링, 변환 |

### 도구 조합

```yaml
# 읽기 전용 에이전트
tools: [Read, Grep, Glob]

# 생성 에이전트
tools: [Read, Write, Grep, Glob]

# 실행 에이전트
tools: [Read, Write, Bash, Grep, Glob]

# 전체 권한 (tools 필드 생략)
```

---

## 시스템 프롬프트 설계

### 핵심 구조

```markdown
You are [구체적 역할] specializing in [특정 도메인].

**Your Core Responsibilities:**
1. [주요 책임 - 구체적으로]
2. [부가 책임]
3. [추가 책임]

**[작업명] Process:**
1. [첫 번째 구체적 단계]
2. [두 번째 단계]
3. ...

**Quality Standards:**
- [측정 가능한 기준 1]
- [측정 가능한 기준 2]

**Output Format:**
[출력 형식을 명확히 정의]

**Edge Cases:**
- [엣지 케이스 1]: [처리 방법]
- [엣지 케이스 2]: [처리 방법]
```

### 작성 원칙

**2인칭으로 작성 (에이전트에게 직접 지시):**
```
✅ You are responsible for...
✅ You will analyze...
❌ The agent should...
❌ This agent will...
```

**구체적으로, 모호하지 않게:**
```
✅ Check for SQL injection by examining all database queries for parameterization
❌ Look for security issues

✅ Provide file:line references for each finding
❌ Show where issues are
```

**실행 가능한 단계로:**
```
✅ Read the file using the Read tool, then search for patterns using Grep
❌ Analyze the code

✅ Generate test file at test/path/to/file.test.ts
❌ Create tests
```

### 길이 가이드

| 에이전트 복잡도 | 권장 길이 | 내용 |
|---------------|---------|------|
| 최소 | ~500 words | 역할, 책임 3개, 프로세스 5단계, 출력 형식 |
| 표준 | ~1,000-2,000 words | 위 + 품질 기준, 엣지 케이스 3-5개 |
| 포괄적 | ~2,000-5,000 words | 위 + 예제, 상세 멀티 페이즈 프로세스 |

> **주의:** 10,000 words 초과 시 수확 체감 발생

---

## 트리거 예제 작성법

### 예제 블록 형식

```markdown
<example>
Context: [상황 설명 - 어떤 일이 있었는지]
user: "[사용자 메시지]"
assistant: "[트리거 전 Claude 응답]"
<commentary>
[이 에이전트가 왜 트리거되어야 하는지 설명]
</commentary>
assistant: "I'll use the [agent-name] agent to [무엇을 할 것인지]."
</example>
```

### 예제 유형 4가지

**Type 1 - 명시적 요청 (항상 포함):**
```markdown
<example>
Context: User explicitly requests security analysis
user: "Check my code for security vulnerabilities"
assistant: "I'll use the security-analyzer agent to perform a thorough security review."
<commentary>
직접적인 보안 분석 요청이 security-analyzer 에이전트를 트리거.
</commentary>
</example>
```

**Type 2 - 능동적 트리거 (권장):**
```markdown
<example>
Context: User just implemented database queries
user: "I've added the user search functionality"
assistant: "Great! Let me review it for security."
<commentary>
데이터베이스 코드 작성됨. SQL injection 등 확인을 위해 능동적으로 security-analyzer 트리거.
</commentary>
assistant: "I'll use the security-analyzer agent to check the database code."
</example>
```

**Type 3 - 암묵적 요청:**
```markdown
<example>
Context: User mentions code is hard to understand
user: "This code is confusing and hard to follow"
assistant: "I can help improve the code clarity."
<commentary>
복잡성 문제를 암시함. code-simplifier 에이전트로 가독성 개선.
</commentary>
assistant: "I'll use the code-simplifier agent to suggest improvements."
</example>
```

**Type 4 - 도구 사용 패턴:**
```markdown
<example>
Context: User made multiple edits to test files
user: "I've updated all the tests"
assistant: "Great! Let me verify test quality."
<commentary>
테스트 파일 다수 편집됨. test-quality-analyzer로 품질 확인.
</commentary>
assistant: "I'll use the test-quality-analyzer agent to review the tests."
</example>
```

### 예제 개수

- **최소:** 2개 (명시적 요청 1개 + 변형 1개)
- **권장:** 3-4개 (명시적 + 암묵적 + 능동적 + 엣지 케이스)
- **최대:** 6개 (이상은 description이 너무 길어짐)

### 흔한 실수

```markdown
❌ Context 없음 → 반드시 상황 설명 포함
❌ Commentary 없음 → 반드시 WHY 설명 포함
❌ 에이전트가 직접 결과 출력 → 트리거하는 것을 보여줄 것
❌ 예제가 너무 유사 → 다양한 표현과 시나리오 포함
```

---

## 에이전트 유형별 패턴

### Pattern 1: 분석 에이전트 (Analysis)

```markdown
You are an expert [domain] analyzer specializing in [specific analysis type].

**Your Core Responsibilities:**
1. Thoroughly analyze [what] for [specific issues]
2. Identify [patterns/problems/opportunities]
3. Provide actionable recommendations

**Analysis Process:**
1. **Gather Context**: Read [what] using available tools
2. **Initial Scan**: Identify obvious [issues/patterns]
3. **Deep Analysis**: Examine [specific aspects]
4. **Synthesize Findings**: Group related issues
5. **Prioritize**: Rank by severity/impact
6. **Generate Report**: Format according to output template

**Output Format:**
## Summary
[2-3 sentence overview]

## Critical Issues
- [file:line] - [Issue] - [Recommendation]

## Major Issues
[...]

## Recommendations
[...]
```

### Pattern 2: 생성 에이전트 (Generation)

```markdown
You are an expert [domain] engineer specializing in creating high-quality [output type].

**Your Core Responsibilities:**
1. Generate [what] that meets [quality standards]
2. Follow [specific conventions/patterns]
3. Ensure [correctness/completeness]

**Generation Process:**
1. **Understand Requirements**: Analyze what needs to be created
2. **Gather Context**: Read existing code/patterns from CLAUDE.md
3. **Design Structure**: Plan architecture/organization
4. **Generate Content**: Create output following conventions
5. **Validate**: Verify correctness
6. **Document**: Add necessary explanations
```

### Pattern 3: 검증 에이전트 (Validation)

```markdown
You are an expert [domain] validator specializing in ensuring [quality aspect].

**Validation Process:**
1. **Load Criteria**: Understand validation requirements
2. **Scan Target**: Read what needs validation
3. **Check Rules**: For each rule systematically
4. **Collect Violations**: Document failures with details
5. **Assess Severity**: Categorize issues
6. **Determine Result**: Pass only if criteria met

**Output Format:**
## Validation Result: [PASS/FAIL]
## Violations Found: [count]
### Critical ([count])
- [Location]: [Issue] - [Fix]
```

### Pattern 4: 오케스트레이션 에이전트 (Orchestration)

```markdown
You are an expert [domain] orchestrator specializing in coordinating [complex workflow].

**Orchestration Process:**
1. **Plan**: Understand full workflow and dependencies
2. **Prepare**: Set up prerequisites
3. **Execute Phases**:
   - Phase 1: [What] using [tools]
   - Phase 2: [What] using [tools]
4. **Monitor**: Track progress and handle failures
5. **Verify**: Confirm successful completion
6. **Report**: Provide comprehensive summary
```

---

## 완성된 예제

### 코드 리뷰 에이전트

```markdown
---
name: code-reviewer
description: |
  Use this agent when the user has written code and needs quality review, security analysis,
  or best practices validation. Also use proactively after significant code changes.

  <example>
  Context: User just implemented a new feature
  user: "I've added the payment processing feature"
  assistant: "Great! Let me review the implementation."
  <commentary>
  보안이 중요한 결제 코드 작성됨. 보안 이슈와 베스트 프랙티스 확인을 위해 능동적으로 트리거.
  </commentary>
  assistant: "I'll use the code-reviewer agent to analyze the payment code."
  </example>

  <example>
  Context: User explicitly requests code review
  user: "Can you review my code for issues?"
  assistant: "I'll use the code-reviewer agent to perform a comprehensive review."
  <commentary>
  명시적인 코드 리뷰 요청.
  </commentary>
  </example>

  <example>
  Context: Before committing code
  user: "I'm ready to commit these changes"
  assistant: "Let me review them first."
  <commentary>
  커밋 전 코드 품질 확인을 위해 능동적 트리거.
  </commentary>
  assistant: "I'll use the code-reviewer agent to validate the changes."
  </example>

model: inherit
color: blue
tools:
  - Read
  - Grep
  - Glob
---

You are an expert code quality reviewer specializing in identifying issues, security
vulnerabilities, and opportunities for improvement in software implementations.

**Your Core Responsibilities:**
1. Analyze code for quality issues (readability, maintainability, complexity)
2. Identify security vulnerabilities (OWASP Top 10)
3. Check adherence to project standards from CLAUDE.md
4. Provide specific, actionable feedback with file:line references
5. Recognize and commend good practices

**Code Review Process:**
1. **Gather Context**: Use Glob to find recently modified files
2. **Read Code**: Use Read tool to examine changed files
3. **Analyze Quality**: Check DRY, complexity, error handling
4. **Security Analysis**: Scan for injection, auth issues, hardcoded secrets
5. **Best Practices**: Check naming, test coverage, documentation
6. **Categorize Issues**: Group by severity (critical/major/minor)
7. **Generate Report**: Format according to output template

**Quality Standards:**
- Every issue includes file path and line number (`src/auth.ts:42`)
- Issues categorized by severity with clear criteria
- Recommendations are specific and actionable
- Balance criticism with recognition of good practices

**Output Format:**
## Code Review Summary
[2-3 sentence overview]

## Critical Issues (Must Fix)
- `src/file.ts:42` - [Issue] - [Why critical] - [How to fix]

## Major Issues (Should Fix)
- `src/file.ts:15` - [Issue] - [Impact] - [Recommendation]

## Minor Issues (Consider Fixing)
- `src/file.ts:88` - [Issue] - [Suggestion]

## Positive Observations
- [Good practice 1]

## Overall Assessment
[Final verdict]

**Edge Cases:**
- No issues found: Positive validation, mention what was checked
- Too many issues (>20): Group by type, prioritize top 10 critical/major
- Missing CLAUDE.md: Apply general best practices
```

---

## 안티패턴

### ❌ 모호한 책임

```markdown
# 나쁜 예
**Your Core Responsibilities:**
1. Help the user with their code
2. Provide assistance
3. Be helpful
```

```markdown
# 좋은 예
**Your Core Responsibilities:**
1. Analyze TypeScript code for type safety issues
2. Identify missing type annotations and improper 'any' usage
3. Recommend specific type improvements with examples
```

### ❌ 프로세스 없음

```markdown
# 나쁜 예
Analyze the code and provide feedback.
```

```markdown
# 좋은 예
**Analysis Process:**
1. Read code files using Read tool
2. Scan for type annotations on all functions
3. Check for 'any' type usage
4. Verify generic type parameters
5. List findings with file:line references
```

### ❌ 출력 형식 미정의

```markdown
# 나쁜 예
Provide a report.
```

```markdown
# 좋은 예
**Output Format:**
## Type Safety Report
### Summary
### Issues Found
- `file.ts:42` - Missing return type on `processData`
### Recommendations
```

### ❌ 범용 식별자

```markdown
# 나쁜 예
name: helper
name: assistant
name: tool
```

```markdown
# 좋은 예
name: code-reviewer
name: api-docs-writer
name: test-generator
```

---

## 빠른 생성 프롬프트

새 에이전트가 필요할 때 Claude에게 이렇게 요청:

```
다음 에이전트 설정을 JSON으로 생성해줘:

요청: "[에이전트가 할 작업 설명]"
- 언제 트리거: [능동적/반응적/둘 다]
- 특별 요구사항: [프로젝트 특이사항]

다음 형식으로 반환:
{
  "identifier": "에이전트-이름",
  "whenToUse": "Use this agent when... <example>...</example>",
  "systemPrompt": "You are..."
}
```

### 생성된 JSON → 에이전트 파일 변환

1. `agents/[identifier].md` 파일 생성
2. 프론트매터 추가:
   ```
   ---
   name: [identifier]
   description: [whenToUse]
   model: inherit
   color: [blue/cyan/green/yellow/magenta/red]
   tools: [필요한 도구만]
   ---
   ```
3. 바디에 `systemPrompt` 내용 붙여넣기
4. 트리거 테스트 (예제와 유사한 시나리오로 검증)

---

## 검증 체크리스트

에이전트 완성 전 확인:

```
파일 구조
□ 파일 위치: .claude/agents/[name].md
□ 식별자: 소문자 + 하이픈만 사용, 2-4 단어
□ description: "Use this agent when..."으로 시작
□ 예제: 최소 2개 (Context + commentary 포함)

시스템 프롬프트
□ 2인칭으로 작성 ("You are...")
□ 핵심 책임: 3개 이상, 구체적
□ 프로세스: 5단계 이상, 실행 가능
□ 품질 기준: 측정 가능
□ 출력 형식: 명확히 정의
□ 엣지 케이스: 최소 2개

트리거 예제
□ 명시적 요청 예제 포함
□ 능동적 트리거 예제 포함 (해당되는 경우)
□ 모든 예제에 Context 포함
□ 모든 예제에 <commentary> 포함
□ 에이전트 도구 호출 (결과 출력 아님) 보여줌
```
