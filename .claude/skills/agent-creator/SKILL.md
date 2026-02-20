---
name: agent-creator
description: |
  This skill should be used when the user wants to create a new Claude Code agent,
  asks how to make an agent, or requests help designing an agent's system prompt,
  description, triggering examples, or file structure.
  Provides complete best practices guide for agent creation.
---

# Agent Creator

Provides best practices and complete guidance for creating Claude Code agents.

## When to Use

- User asks to create a new agent
- User asks how to write an agent system prompt
- User asks about agent file structure or frontmatter
- User asks about agent triggering examples

## How to Use

Load `references/guide.md` for the complete agent creation guide, which includes:

- File structure and frontmatter reference
- System prompt design patterns (Analysis / Generation / Validation / Orchestration)
- Triggering example best practices with all 4 example types
- Complete working example (code-reviewer agent)
- Anti-patterns to avoid
- Quick generation prompt template
- Validation checklist

## Process

1. Load `references/guide.md` to access the full guide
2. Understand what kind of agent the user wants to create
3. Follow the guide's patterns to design the agent
4. Apply the validation checklist before finalizing
5. Save the agent file to `.claude/agents/[name].md`
