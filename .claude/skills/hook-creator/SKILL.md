---
name: hook-creator
description: |
  This skill should be used when the user wants to create a Claude Code hook,
  asks how to set up automation with hooks, or requests help writing hook scripts
  for PreToolUse, PostToolUse, Stop, SessionStart, or Notification events.
  Provides complete best practices guide for hook creation.
---

# Hook Creator

Provides best practices and complete guidance for creating Claude Code hooks.

## When to Use

- User asks to create a new hook
- User asks how to block or validate tool usage
- User asks about hook events or settings.json configuration
- User asks about automating tasks on session start or tool execution
- User wants to enforce quality standards (test enforcement, build verification)

## How to Use

Load `references/guide.md` for the complete hook creation guide, which includes:

- All 5 hook event types and their use cases
- Hook settings structure and file locations
- command vs prompt type selection guide
- Response format (exit codes, JSON output)
- Matcher patterns
- 10 common patterns (security, test enforcement, lint, audit log, etc.)
- Advanced patterns (multi-stage, cross-event workflows)
- Environment variable reference
- Validation checklist

## Process

1. Load `references/guide.md` to access the full guide
2. Identify which event type fits the user's need
3. Choose command or prompt type based on complexity
4. Write the hook script or prompt following the patterns
5. Register in the appropriate settings file (global / project / local)
6. Apply the validation checklist before finalizing
