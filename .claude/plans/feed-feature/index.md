# 피드 추가 기능 개발 계획

## Context

### Original Request
피드 도메인에 좋아요, 댓글, 랭킹, 피드 현황 기능을 추가한다.

### Codebase Analysis
- **아키텍처**: Api(interface) -> Controller -> Facade Service -> General Service -> Repository -> Entity
- **패키지 경로**: `ddingdong.ddingdongBE.domain.feed`
- **인증 체계**: Spring Security + JWT, `PrincipalDetails`로 User 추출
- **권한 분리**:
  - `/server/**` (GET): 비인증 사용자 허용
  - `/server/central/**`: ROLE_CLUB (동아리 회장)
  - `/server/admin/**`: ROLE_ADMIN (총동연)
- **기존 패턴**: Soft delete (`@SQLDelete` + `deleted_at`), `BaseEntity`(createdAt/updatedAt), record 기반 DTO
- **테스트**: SpringBootTest + TestContainerSupport + FixtureMonkey + Mockito
- **DB 마이그레이션**: Flyway, 현재 V53까지 존재

### Research Findings
- FixZoneComment 엔티티가 댓글 패턴의 참조 모델 (User + 부모 엔티티 ManyToOne)
- Feed 엔티티는 Club과 ManyToOne 관계, viewCount 컬럼 없음 (추가 필요)
- 좋아요는 User + Feed의 유니크 조합으로 중복 방지 필요
- 랭킹은 viewCount + commentCount + likeCount 기반 총점 계산

## Work Objectives

### Core Objective
피드에 좋아요/댓글 기능을 추가하고, 총동연과 동아리 회장을 위한 피드 랭킹/현황 조회 API를 구현한다.

### Deliverables
1. FeedComment, FeedLike 엔티티 및 Flyway 마이그레이션
2. 피드 좋아요 생성/취소 API (인증 필요)
3. 피드 댓글 작성/삭제 API (인증 필요)
4. 월별 피드 랭킹 조회 API (총동연)
5. 지난 피드 랭킹 1위 조회 API (총동연)
6. 동아리 회장 전체 피드 랭킹 조회 API
7. 동아리 회장 이달의 피드 현황 조회 API
8. 기존 피드 조회 API에 좋아요수/댓글수 추가
9. Notion 문서 업데이트

### Definition of Done
- 모든 신규 API가 Swagger에 노출되고 정상 동작
- 기존 피드 조회 API 응답에 likeCount, commentCount 포함
- Flyway 마이그레이션 정상 실행
- 단위/통합 테스트 통과
- Notion ERD 및 API 명세 업데이트 완료

## Must Have
- FeedComment, FeedLike 엔티티에 Soft delete 적용
- 좋아요 중복 방지 (User + Feed unique constraint)
- 랭킹 총점 계산 공식 명확히 정의
- 기존 API의 하위 호환성 유지 (필드 추가만, 제거 없음)
- Feed 엔티티에 viewCount 필드 추가

## Must NOT Have
- 좋아요/댓글에 대한 알림(Notification) 기능 (별도 이슈)
- 댓글 수정 기능 (요구사항에 없음)
- 대댓글 기능
- 좋아요한 사용자 목록 조회 API

## Task Flow and Dependencies

```
Task 1 (DB Migration)
  |
  v
Task 2 (Entity)
  |
  v
Task 3 (Repository) ----+
  |                      |
  v                      v
Task 4 (Service)    Task 5 (Feed Ranking Service)
  |                      |
  v                      v
Task 6 (Like/Comment API)  Task 7 (Ranking API)  Task 8 (Club Feed Status API)
  |                      |                         |
  v                      v                         v
Task 9 (Modify Existing APIs)
  |
  v
Task 10 (Security Config Update)
  |
  v
Task 11 (Tests)
  |
  v
Task 12 (Notion Update)
```

## Task List

| Task | File | Description |
|------|------|-------------|
| 01 | `01-db-migration.md` | Flyway 마이그레이션 스크립트 작성 |
| 02 | `02-entity.md` | FeedComment, FeedLike 엔티티 + Feed viewCount 추가 |
| 03 | `03-repository.md` | Repository 인터페이스 작성 |
| 04 | `04-service-like-comment.md` | 좋아요/댓글 Service 레이어 |
| 05 | `05-service-ranking.md` | 피드 랭킹 Service 레이어 |
| 06 | `06-api-like-comment.md` | 좋아요/댓글 API (Controller + Api interface) |
| 07 | `07-api-ranking.md` | 총동연 피드 랭킹 API |
| 08 | `08-api-club-feed-status.md` | 동아리 회장 피드 현황 API |
| 09 | `09-modify-existing-apis.md` | 기존 피드 조회 API 수정 |
| 10 | `10-security-config.md` | SecurityConfig 라우팅 업데이트 |
| 11 | `11-tests.md` | 테스트 코드 작성 |
| 12 | `12-notion-update.md` | Notion ERD/API 명세 업데이트 |

## Commit Strategy

1. `[DDING-000] 피드 좋아요, 댓글 테이블 Flyway 마이그레이션 추가` (Task 01)
2. `[DDING-000] FeedComment, FeedLike 엔티티 및 Repository 추가` (Task 02-03)
3. `[DDING-000] 피드 좋아요/댓글 Service 레이어 구현` (Task 04)
4. `[DDING-000] 피드 랭킹 Service 레이어 구현` (Task 05)
5. `[DDING-000] 피드 좋아요/댓글 API 구현` (Task 06)
6. `[DDING-000] 총동연 피드 랭킹 API 구현` (Task 07)
7. `[DDING-000] 동아리 회장 피드 현황 API 구현` (Task 08)
8. `[DDING-000] 기존 피드 조회 API에 좋아요수/댓글수 추가` (Task 09)
9. `[DDING-000] SecurityConfig 라우팅 업데이트` (Task 10)
10. `[DDING-000] 피드 추가 기능 테스트 코드 작성` (Task 11)

## Success Criteria
- 모든 신규 API 정상 동작 확인 (Swagger 테스트)
- 기존 피드 조회 API 하위 호환성 유지
- 좋아요 중복 생성 시 적절한 에러 응답
- 랭킹 조회 시 총점 기준 정렬 정확성
- Flyway 마이그레이션 롤백 없이 정상 실행
- 테스트 커버리지: 신규 Service 레이어 100%
