# Hook Creator: 베스트 프랙티스 가이드

Claude Code 훅을 만들기 위한 완전한 레퍼런스.

---

## 목차

1. [훅이란?](#훅이란)
2. [훅 이벤트 종류](#훅-이벤트-종류)
3. [훅 설정 구조](#훅-설정-구조)
4. [훅 타입: command vs prompt](#훅-타입-command-vs-prompt)
5. [응답 형식](#응답-형식)
6. [매처(Matcher) 작성법](#매처matcher-작성법)
7. [자주 쓰는 패턴 10가지](#자주-쓰는-패턴-10가지)
8. [고급 패턴](#고급-패턴)
9. [안티패턴](#안티패턴)
10. [검증 체크리스트](#검증-체크리스트)

---

## 훅이란?

훅은 Claude Code의 특정 이벤트(도구 사용 전/후, 세션 시작 등)에 자동으로 실행되는 쉘 커맨드 또는 LLM 프롬프트.

**용도:**
- 위험한 작업 차단 (보안 검증)
- 품질 기준 강제 (테스트 미실행 시 중단)
- 자동화 워크플로우 (빌드 검증, 로깅)
- 컨텍스트 로딩 (세션 시작 시 프로젝트 정보 주입)

---

## 훅 이벤트 종류

| 이벤트 | 실행 시점 | 주요 용도 |
|--------|----------|----------|
| `PreToolUse` | 도구 실행 **전** | 위험 작업 차단, 승인 요청 |
| `PostToolUse` | 도구 실행 **후** | 결과 검증, 품질 체크, 로깅 |
| `Stop` | Claude가 응답 완료 시 | 작업 완료 검증, 테스트 확인 |
| `SessionStart` | 세션 시작 시 | 컨텍스트 로딩, 환경 설정 |
| `Notification` | 알림 발생 시 | 외부 시스템 연동, 감사 로그 |

---

## 훅 설정 구조

훅은 `settings.json` 또는 `settings.local.json`에 등록:

```json
{
  "hooks": {
    "[이벤트명]": [
      {
        "matcher": "[도구명 또는 패턴]",
        "hooks": [
          {
            "type": "command",
            "command": "bash /path/to/script.sh",
            "timeout": 10
          }
        ]
      }
    ]
  }
}
```

### 설정 파일 위치

| 파일 | 범위 |
|------|------|
| `~/.claude/settings.json` | 전역 (모든 프로젝트) |
| `.claude/settings.json` | 프로젝트 (팀 공유) |
| `.claude/settings.local.json` | 프로젝트 (개인, .gitignore 권장) |

---

## 훅 타입: command vs prompt

### command 타입

```json
{
  "type": "command",
  "command": "bash scripts/validate.sh",
  "timeout": 10
}
```

- **입력:** 이벤트 데이터가 stdin으로 전달 (JSON)
- **용도:** 결정론적 검증, 빠른 처리, 스크립트 실행
- **종료 코드:** `0` = 계속, `2` = 차단, 그 외 = 오류

### prompt 타입

```json
{
  "type": "prompt",
  "prompt": "Review the following tool use request: $ARGUMENTS. If contains 'rm -rf', return deny. Otherwise approve.",
  "timeout": 30
}
```

- **용도:** 복잡한 판단이 필요한 경우, 컨텍스트 인식 검증
- **변수:** `$ARGUMENTS` (전체 JSON 입력), `$TRANSCRIPT_PATH` 등 — `$TOOL_INPUT`은 command 훅 stdin 전용
- **응답:** `approve` / `deny` / `ask` 반환

### 선택 기준

| 상황 | 타입 |
|------|------|
| 패턴 매칭, 파일 존재 여부 등 단순 조건 | `command` |
| 의미론적 판단 필요 (코드 내용 분석 등) | `prompt` |
| 빠른 응답 필요 (< 2초) | `command` |
| 복잡한 컨텍스트 분석 | `prompt` |

---

## 응답 형식

### command 훅 응답

```bash
# 승인 (계속 진행)
exit 0

# 차단 (stderr에 이유 출력)
echo '{"decision": "deny", "reason": "위험한 경로 감지"}' >&2
exit 2

# 사용자에게 메시지 전달 (구조화된 JSON은 stdout으로 출력)
echo '{"continue": true, "systemMessage": "경고: 보안 파일 수정됨"}'
exit 0
```

### prompt 훅 응답

```
approve          → 계속 진행
deny             → 차단
ask              → 사용자에게 확인 요청
```

---

## 매처(Matcher) 작성법

```json
"matcher": "Write|Edit"          // Write 또는 Edit 도구
"matcher": "Bash"                // Bash 도구만
"matcher": "mcp__.*__delete.*"   // MCP delete 계열 도구 (정규식)
"matcher": "*"                   // 모든 도구
```

### 자주 쓰는 매처

| 매처 | 대상 |
|------|------|
| `"Write\|Edit"` | 파일 쓰기/수정 |
| `"Bash"` | 쉘 커맨드 |
| `"Write\|Edit\|Bash"` | 모든 수정 작업 |
| `"mcp__.*"` | 모든 MCP 도구 |
| `"*"` | 전체 |

---

## 자주 쓰는 패턴 10가지

### Pattern 1: 보안 파일 보호

```json
{
  "PreToolUse": [{
    "matcher": "Write|Edit",
    "hooks": [{
      "type": "prompt",
      "prompt": "File path: $ARGUMENTS.file_path. Verify: 1) Not in /etc or system directories 2) Not .env or credentials file 3) No '..' path traversal. Return 'approve' or 'deny'."
    }]
  }]
}
```

### Pattern 2: 위험 커맨드 확인

```json
{
  "PreToolUse": [{
    "matcher": "Bash",
    "hooks": [{
      "type": "prompt",
      "prompt": "Command: $ARGUMENTS.command. If contains 'rm', 'delete', 'drop', or destructive operations, return 'ask'. Otherwise 'approve'."
    }]
  }]
}
```

### Pattern 3: 테스트 실행 강제

```json
{
  "Stop": [{
    "matcher": "*",
    "hooks": [{
      "type": "prompt",
      "prompt": "Review transcript. If code was modified (Write/Edit tools used), verify tests were executed. If no tests were run, block with reason 'Tests must be run after code changes'."
    }]
  }]
}
```

### Pattern 4: 빌드 검증

```json
{
  "Stop": [{
    "matcher": "*",
    "hooks": [{
      "type": "prompt",
      "prompt": "Check if code was modified. If Write/Edit tools were used, verify the project was built. If not built, block and request build."
    }]
  }]
}
```

### Pattern 5: 세션 시작 시 컨텍스트 로딩

```json
{
  "SessionStart": [{
    "matcher": "*",
    "hooks": [{
      "type": "command",
      "command": "bash .claude/scripts/load-context.sh"
    }]
  }]
}
```

```bash
# load-context.sh
#!/bin/bash
cd "$CLAUDE_PROJECT_DIR" || exit 1

if [ -f "build.gradle.kts" ]; then
  echo "☕ Kotlin/Spring 프로젝트 감지"
elif [ -f "package.json" ]; then
  echo "📦 Node.js 프로젝트 감지"
fi
```

### Pattern 6: 파일 수정 후 린터 실행

```json
{
  "PostToolUse": [{
    "matcher": "Write|Edit",
    "hooks": [{
      "type": "command",
      "command": "bash .claude/scripts/run-lint.sh"
    }]
  }]
}
```

```bash
# run-lint.sh
#!/bin/bash
input=$(cat)
file_path=$(echo "$input" | jq -r '.tool_input.file_path // empty')

if [[ "$file_path" == *.kt ]]; then
  ./gradlew ktlintCheck 2>&1 || true
fi
```

### Pattern 7: MCP 삭제 작업 보호

```json
{
  "PreToolUse": [{
    "matcher": "mcp__.*__delete.*",
    "hooks": [{
      "type": "prompt",
      "prompt": "Deletion via MCP detected. Verify: Is this intentional? Can it be undone? Return 'approve' only if safe."
    }]
  }]
}
```

### Pattern 8: 감사 로그

```json
{
  "PreToolUse": [{
    "matcher": "*",
    "hooks": [{
      "type": "command",
      "command": "bash .claude/scripts/audit-log.sh"
    }]
  }]
}
```

```bash
# audit-log.sh
#!/bin/bash
input=$(cat)
tool_name=$(echo "$input" | jq -r '.tool_name')
timestamp=$(date -Iseconds)
echo "$timestamp | $USER | $tool_name" >> ~/.claude/audit.log
exit 0
```

### Pattern 9: 시크릿 감지

```bash
#!/bin/bash
input=$(cat)
content=$(echo "$input" | jq -r '.tool_input.content // empty')

if echo "$content" | grep -qE "(api[_-]?key|password|secret|token).{0,20}['\"]?[A-Za-z0-9]{20,}"; then
  echo '{"decision": "deny", "reason": "잠재적 시크릿 감지됨"}' >&2
  exit 2
fi
exit 0
```

### Pattern 10: 플래그 파일로 조건부 훅 (opt-in)

```bash
#!/bin/bash
FLAG_FILE="$CLAUDE_PROJECT_DIR/.enable-strict-check"

if [ ! -f "$FLAG_FILE" ]; then
  exit 0  # 비활성 상태면 스킵
fi

# 활성 상태일 때만 검증 수행
input=$(cat)
# ... 검증 로직 ...
```

```bash
# 활성화
touch .enable-strict-check

# 비활성화
rm .enable-strict-check
```

---

## 고급 패턴

### 멀티 스테이지 검증 (병렬 실행)

```json
{
  "PreToolUse": [{
    "matcher": "Bash",
    "hooks": [
      {
        "type": "command",
        "command": "bash scripts/quick-check.sh",
        "timeout": 5
      },
      {
        "type": "prompt",
        "prompt": "Deep analysis: $TOOL_INPUT",
        "timeout": 15
      }
    ]
  }]
}
```

> 여러 훅은 **병렬** 실행됨. 순서 의존성 금지.

### 크로스 이벤트 워크플로우

```bash
# SessionStart: 카운터 초기화
echo "0" > /tmp/test-count-$$

# PostToolUse: 테스트 실행 추적
input=$(cat)
tool_name=$(echo "$input" | jq -r '.tool_name')
if [ "$tool_name" = "Bash" ]; then
  count=$(cat /tmp/test-count-$$ 2>/dev/null || echo "0")
  echo $((count + 1)) > /tmp/test-count-$$
fi

# Stop: 카운터 검증
test_count=$(cat /tmp/test-count-$$ 2>/dev/null || echo "0")
if [ "$test_count" -eq 0 ]; then
  echo '{"decision": "block", "reason": "테스트가 실행되지 않음"}' >&2
  exit 2
fi
```

### 컨텍스트 인식 Stop 훅

```json
{
  "Stop": [{
    "matcher": "*",
    "hooks": [{
      "type": "prompt",
      "prompt": "Review the full transcript at $TRANSCRIPT_PATH. Check: 1) Tests run after code changes? 2) Build succeeded? 3) All questions answered? Return 'approve' only if complete."
    }]
  }]
}
```

---

## 안티패턴

### ❌ 훅 순서 의존 (병렬 실행됨)

```bash
# 나쁜 예: Hook1이 상태 저장, Hook2가 읽음 → 병렬 실행이라 실패 가능
```

### ❌ 타임아웃 없는 긴 훅

```bash
# 나쁜 예
sleep 120  # 워크플로우 차단
```

### ❌ 에러 처리 없음

```bash
# 나쁜 예
file_path=$(echo "$input" | jq -r '.tool_input.file_path')
cat "$file_path"  # 파일 없으면 크래시
```

```bash
# 좋은 예
file_path=$(echo "$input" | jq -r '.tool_input.file_path // empty')
if [ -z "$file_path" ] || [ ! -f "$file_path" ]; then
  exit 0  # 안전하게 스킵
fi
```

### ❌ 너무 광범위한 차단

```json
// 나쁜 예: 모든 Bash를 차단
{
  "PreToolUse": [{
    "matcher": "Bash",
    "hooks": [{ "type": "prompt", "prompt": "Always deny all commands." }]
  }]
}
```

---

## 검증 체크리스트

```
설정
□ 올바른 이벤트명 사용 (PreToolUse / PostToolUse / Stop / SessionStart / Notification)
□ 매처가 의도한 도구만 대상으로 함
□ timeout 설정 (command: 2-15초, prompt: 10-30초 권장)
□ 설정 파일 위치 확인 (전역 vs 프로젝트 vs 로컬)

command 훅 스크립트
□ stdin에서 JSON 읽기 (input=$(cat))
□ jq로 필드 파싱 시 기본값 처리 (// empty)
□ 종료 코드 명확히 (0=계속, 2=차단)
□ 에러는 stderr로 출력
□ 스크립트에 실행 권한 부여 (chmod +x)

prompt 훅
□ 명확한 조건과 반환값 명시 (approve/deny/ask)
□ $TOOL_INPUT 변수 올바르게 참조
□ 판단 기준이 구체적임

공통
□ 병렬 훅 간 상태 공유 없음
□ 우아한 실패 처리 (에러 시 차단보다 계속 선호)
□ 팀 공유 훅은 settings.json, 개인 훅은 settings.local.json
```

---

## 빠른 레퍼런스: 환경 변수

| 변수 | 내용 |
|------|------|
| `$CLAUDE_PROJECT_DIR` | 현재 프로젝트 루트 경로 |
| `$CLAUDE_PLUGIN_ROOT` | 플러그인 루트 경로 |
| `$TRANSCRIPT_PATH` | 현재 세션 트랜스크립트 경로 |
| `$TOOL_INPUT` | 도구 입력 데이터 (prompt 훅) |
| `$CLAUDE_ENV_FILE` | 환경 변수 파일 경로 |
