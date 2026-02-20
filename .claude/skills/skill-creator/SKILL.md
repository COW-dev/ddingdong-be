---
name: skill-creator
description: |
  This skill should be used when the user wants to create a new Claude Code skill,
  asks how to package domain knowledge or workflows into a skill, or requests help
  writing SKILL.md, organizing bundled resources (scripts, references, assets),
  or understanding the skill structure.
  Provides complete best practices guide for skill creation.
---

# Skill Creator

Provides best practices and complete guidance for creating Claude Code skills.

## When to Use

- User asks to create a new skill
- User asks how to write SKILL.md
- User asks about organizing scripts, references, or assets
- User asks about the difference between skills and agents
- User wants to package reusable workflows or domain knowledge

## How to Use

Load `references/guide.md` for the complete skill creation guide, which includes:

- Directory structure (`SKILL.md` + `scripts/` + `references/` + `assets/`)
- SKILL.md frontmatter and writing style (imperative form, not second-person)
- Three resource types and when to use each
- Progressive Disclosure principle (3-level loading)
- 6-step skill creation process
- Complete working example (Spring API docs skill)
- Anti-patterns to avoid (duplication, second-person style, bloated SKILL.md)
- Skill vs Agent selection guide
- Validation checklist

## Process

1. Load `references/guide.md` to access the full guide
2. Follow Step 1-2: gather concrete examples and plan resources
3. Create the skill directory structure
4. Write bundled resources first (scripts/references/assets)
5. Write lean SKILL.md referencing the resources
6. Apply the validation checklist before finalizing
