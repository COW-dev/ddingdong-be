# 피드 추가 기능 개발 계획 (API 단위)

## 구현 현황

### ✅ 완료
| 파일 | 대상 API |
|------|---------|
| `00-api-feed-like.md` | POST/DELETE `/server/feeds/{feedId}/likes` |

### ❌ 미구현
| 파일 | 대상 API |
|------|---------|
| `01-api-feed-comment.md` | POST/DELETE `/server/feeds/{feedId}/comments` |
| `02-api-admin-monthly-ranking.md` | GET `/server/admin/feed-rankings/monthly` |
| `03-api-admin-ranking-winners.md` | GET `/server/admin/feed-rankings/winners` |
| `04-api-club-feed-ranking.md` | GET `/server/central/my/feed-rankings` |
| `05-api-club-monthly-best.md` | GET `/server/central/my/feed-rankings/monthly-best` |
| `06-api-modify-existing.md` | 기존 피드 조회 API 수정 (likeCount/commentCount 추가) |

## 브랜치 전략

각 plan 파일 = 브랜치 1개 = PR 1개. 모두 `develop` 기준으로 분기.

| 브랜치 | 대상 plan | base → target |
|--------|---------|--------------|
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
- **FeedLike 엔티티**: hard delete 방식 (unique constraint 충돌 이슈)
- **FeedComment 엔티티**: soft delete 방식

## 전체 API 목록

| Method | URL | Auth | 상태 |
|--------|-----|------|------|
| POST | `/server/feeds/{feedId}/likes` | USER | ✅ `00` |
| DELETE | `/server/feeds/{feedId}/likes` | USER | ✅ `00` |
| POST | `/server/feeds/{feedId}/comments` | USER | ❌ `01` |
| DELETE | `/server/feeds/{feedId}/comments/{commentId}` | USER | ❌ `01` |
| GET | `/server/admin/feed-rankings/monthly?year=&month=` | ADMIN | ❌ `02` |
| GET | `/server/admin/feed-rankings/winners?year=` | ADMIN | ❌ `03` |
| GET | `/server/central/my/feed-rankings?year=` | CLUB | ❌ `04` |
| GET | `/server/central/my/feed-rankings/monthly-best` | CLUB | ❌ `05` |
| GET | `/server/feeds` (수정) | — | ❌ `06` |
| GET | `/server/feeds/{feedId}` (수정) | — | ❌ `06` |
| GET | `/server/clubs/{clubId}/feeds` (수정) | — | ❌ `06` |
