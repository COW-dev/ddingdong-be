---
name: sync-develop
description: |
  develop 브랜치로 체크아웃하고 최신 코드를 pull 해오는 스킬.
  "develop 동기화", "develop pull", "/sync-develop" 요청 시 사용.
---

# Sync Develop

develop 브랜치로 전환하고 원격 저장소에서 최신 변경사항을 가져온다.

## When to Use

- 새 작업 시작 전 develop 최신화가 필요할 때
- "develop 체크아웃해줘", "develop pull 해줘", "develop 동기화" 요청 시
- "/sync-develop" 슬래시 커맨드 입력 시

## Process

1. **현재 상태 확인**: `git status`로 uncommitted 변경사항 확인
   - 변경사항이 있으면 사용자에게 알리고 stash 여부 확인

2. **develop 체크아웃 및 pull 실행**:
   ```bash
   git checkout develop && git pull
   ```

3. **결과 보고**: pull 결과 요약 (새로 받은 커밋 수, 변경 파일 등)
