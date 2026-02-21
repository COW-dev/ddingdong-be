# 피드 추가 기능 개발 계획 (Notion API 1~11 기준)

## 전체 API 목록

| No | Method | URL | Auth | 상태 | plan 파일 |
|----|--------|-----|------|------|----------|
| 1  | POST   | `/server/feeds/{feedId}/likes` | 비회원 (UUID) | ❌ | `00` |
| 2  | DELETE | `/server/feeds/{feedId}/likes` | 비회원 (UUID) | ❌ | `00` |
| 3  | POST   | `/server/feeds/{feedId}/comments` | 비회원 (UUID) | ❌ | `01` |
| 4  | DELETE | `/server/feeds/{feedId}/comments/{commentId}` | 비회원 (UUID) | ❌ | `01` |
| 5  | DELETE | `/server/central/feeds/{feedId}/comments/{commentId}` | ROLE_CLUB | ❌ | `01` |
| 6  | GET    | `/server/central/feeds/ranking?year=&month=` | ROLE_CLUB | ❌ | `04` |
| 7  | GET    | `/server/central/feeds/status?year=&month=` | ROLE_CLUB | ❌ | `05` |
| 8  | GET    | `/server/admin/feeds/ranking?year=&month=` | ROLE_ADMIN | ❌ | `02` |
| 9  | GET    | `/server/admin/feeds/ranking/last?year=` | ROLE_ADMIN | ❌ | `03` |
| 10 | GET    | `/server/feeds` (수정) | — | ❌ | `06` |
| 11 | GET    | `/server/feeds/{feedId}` (수정) | — | ❌ | `06` |
| 11 | GET    | `/server/clubs/{clubId}/feeds` (수정) | — | ❌ | `06` |

---

## 브랜치 전략

각 plan 파일 = 브랜치 1개 = PR 1개. 모두 `develop` 기준으로 분기.

| 브랜치 | 대상 plan | base → target |
|--------|---------|--------------|
| `feat/DDING-000-feed-like-api` | `00` | develop → develop |
| `feat/DDING-000-feed-comment-api` | `01` | develop → develop |
| `feat/DDING-000-feed-admin-monthly-rank` | `02` | develop → develop |
| `feat/DDING-000-feed-admin-rank-winners` | `03` | develop → develop |
| `feat/DDING-000-feed-club-ranking` | `04` | develop → develop |
| `feat/DDING-000-feed-club-monthly-best` | `05` | develop → develop |
| `feat/DDING-000-feed-modify-existing` | `06` | develop → develop |

> 02~05는 FeedRankingService를 공유하므로 의존 관계 발생 시 먼저 merge 후 분기.

---

## 아키텍처 & 컨텍스트

- **패키지**: `ddingdong.ddingdongBE.domain.feed`
- **Flyway 현황**: V56까지 존재 (feed_like, feed_comment 테이블, feed.view_count 컬럼 완료)
- **Feed 엔티티**: viewCount 필드 이미 추가됨
- **FeedLike**: hard delete 방식 (unique constraint 충돌 이슈) → uuid 컬럼으로 전환 필요
- **FeedComment**: soft delete 방식 → uuid, anonymous_number 컬럼 추가 필요

---

## 공통 기술 사항

### UUID 검증 (API 1~4)
- 헤더: `X-Anonymous-UUID`
- UUID v4 정규식 검증, 실패 시 400
- SecurityConfig: `permitAll` 처리 (비인증 요청 허용)

### Caffeine 캐시 (좋아요 중복 방지)
```groovy
// build.gradle
implementation 'com.github.ben-manes.caffeine:caffeine'
```
- Key: uuid, Value: `Set<Long>` feedIds, TTL: 14일
- 서버 재시작 시 DB로 fallback

### 랭킹 공식 (API 6~9)
```
score = viewCount × 1 + likeCount × 3 + commentCount × 5
```
정렬: score DESC, feedId ASC (동점 시)

### SecurityConfig 변경 필요

```
POST/DELETE /server/feeds/**/likes            → permitAll (비회원)
POST/DELETE /server/feeds/**/comments/**      → permitAll (비회원)
DELETE /server/central/feeds/**/comments/**  → hasRole(CLUB)
GET /server/central/feeds/ranking            → hasRole(CLUB)
GET /server/central/feeds/status             → hasRole(CLUB)
GET /server/admin/feeds/ranking              → hasRole(ADMIN)
GET /server/admin/feeds/ranking/last         → hasRole(ADMIN)
```
