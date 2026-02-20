# CLAUDE.md — ddingdong-be

프로젝트 개요, 구조, 명령어, 핵심 패턴은 @AGENTS.md 참조.

---

## Claude 작업 규칙

### 시작 전
- 요청이 모호하면 먼저 질문한다
- 수정할 파일은 반드시 읽고 기존 패턴을 파악한 뒤 작업한다
- 솔루션 로직을 스스로 검토한 후 제시한다

### 코드 작성
- 기존 프로젝트 스타일과 DDD 구조를 엄격히 따른다
- 요청된 작업 범위만 수정한다 (불필요한 리팩토링 금지)
- 완전히 실행 가능한 코드만 제공한다 (의사코드 금지)
- `@Valid`, `@NotNull` 등 DTO 검증 어노테이션 적용
- secrets/환경변수 하드코딩 절대 금지

### 새 기능 추가 시 필수 순서
1. (DB 변경 시) `resources/db/migration/`에 Flyway 마이그레이션 파일 추가
2. `api/` 패키지에 Swagger 인터페이스 정의
3. `controller/` 구현 (인터페이스 implements)
4. `service/` + command/query DTO 작성
5. `repository/` 쿼리 추가
6. 테스트 작성 (RestAssured + Fixture Monkey)

### 테스트
- 코드 작성 후 `./gradlew test`로 검증한다
- 버그 수정/기능 추가 시 반드시 테스트 추가
- TestContainers 사용 — Docker 실행 상태 필요

---

## Git / PR 규칙

- 브랜치명: `{type}/{DDING-이슈번호}-{설명}` (예: `feat/DDING-123-club-search`)
- 커밋 메시지: 한국어, `[DDING-000] 작업 내용` 형식
- PR 템플릿: 🚀 작업 내용 / 🤔 고민했던 내용 / 💬 리뷰 중점사항

### API 단위 브랜치 전략 (PR 크기 관리)

여러 API를 한 기능에서 개발할 때 PR이 커지는 것을 방지하기 위해 **API 1개 = 브랜치 1개 = PR 1개** 원칙을 따른다.

**규칙**
- 각 브랜치는 plan 파일 1개에 대응 (구현 + 테스트 포함)
- 브랜치는 `develop`에서 분기, `develop`으로 PR
- 의존 관계가 있는 경우 앞 브랜치 merge 후 다음 브랜치 분기
- 브랜치명: `feat/{DDING-이슈번호}-{도메인}-{api-설명}` (예: `feat/DDING-000-feed-comment-api`)

**작업 순서 (피드 추가 기능 예시)**
```
develop
  └─ feat/DDING-000-feed-comment-api          # 01 댓글 작성/삭제
  └─ feat/DDING-000-feed-admin-monthly-rank   # 02 총동연 월별 랭킹
  └─ feat/DDING-000-feed-admin-rank-winners   # 03 총동연 지난 1위
  └─ feat/DDING-000-feed-club-ranking         # 04 동아리 개별 랭킹
  └─ feat/DDING-000-feed-club-monthly-best    # 05 동아리 이달의 현황
  └─ feat/DDING-000-feed-modify-existing      # 06 기존 API 수정
```

---

## 절대 금지

- Flyway 기존 마이그레이션 파일 수정
- 엔티티 물리 삭제 (`DELETE` 직접 실행)
- `api/` 인터페이스 없이 Controller 단독 작성
- application.yml 내 시크릿 값 직접 기재
