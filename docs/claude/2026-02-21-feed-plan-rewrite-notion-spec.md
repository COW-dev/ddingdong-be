# 피드 추가기능 plan 파일 Notion 실제 명세 기준 전면 재작성

> **날짜**: 2026-02-21
> **카테고리**: claude
> **관련 파일**: `.claude/plans/feed-feature/index.md`, `.claude/plans/feed-feature/00-api-feed-like.md`, `.claude/plans/feed-feature/01-api-feed-comment.md`, `.claude/plans/feed-feature/02-api-admin-monthly-ranking.md`, `.claude/plans/feed-feature/03-api-admin-ranking-winners.md`, `.claude/plans/feed-feature/04-api-club-feed-ranking.md`, `.claude/plans/feed-feature/05-api-club-monthly-best.md`, `.claude/plans/feed-feature/06-api-modify-existing.md`

## 작업 요약

Notion 실제 API 명세를 확인한 결과, 기존 plan 파일(00~06.md)이 실제 명세와 크게 달랐다.
`chore/DDING-000-ai-docs` 브랜치에서 8개 plan 파일을 전면 재작성하여 커밋·푸시했다.
구현 코드는 수정하지 않고 문서(plan)만 변경했다.

## 변경 내용

### index.md
- API 1~11 전체 목록 및 각 브랜치명 재정리
- 수정된 URL 전체 반영
- 공통 기술 사항(UUID 검증, Caffeine 캐시, 랭킹 공식, SecurityConfig) 추가

### 00-api-feed-like.md (API 1~2)
- 인증 방식: `USER` JWT → 비회원 `X-Anonymous-UUID` 헤더
- 상태코드: POST 201 → 204, 중복 409 Conflict (기존 400)
- FeedLike 엔티티: `user_id` 컬럼 → `uuid VARCHAR(36)` 로 교체
- Caffeine 캐시: uuid → `Set<Long>` feedIds, TTL 14일
- DB 마이그레이션: `ALTER TABLE feed_like DROP user_id, ADD uuid` 명시

### 01-api-feed-comment.md (API 3~5)
- 인증 방식: `USER` JWT → 비회원 `X-Anonymous-UUID` 헤더
- 댓글 익명 처리: `anonymousNumber` 필드, 피드별 MAX+1, 동일 UUID 재방문 시 재사용
- POST 응답: `{ commentId, anonymousNumber }` 추가
- API 5 신규: `DELETE /server/central/feeds/{feedId}/comments/{commentId}` (ROLE_CLUB 강제삭제)
- FeedComment 엔티티: `user_id` 제거 → `uuid`, `anonymous_number` 추가

### 02-api-admin-monthly-ranking.md (API 8)
- URL: `/server/admin/feed-rankings/monthly` → `/server/admin/feeds/ranking`
- 랭킹 공식: `view+like+comment` → `view×1 + like×3 + comment×5`
- 응답 필드: `activityContent`, `feedType` 제거 → `thumbnailUrl` 추가

### 03-api-admin-ranking-winners.md (API 9)
- URL: `/server/admin/feed-rankings/winners` → `/server/admin/feeds/ranking/last`
- 응답 구조: 월별 1위 목록(최대 12개) → **연도 전체 절대 1위 단건**
- 응답 필드에 `targetYear`, `targetMonth` 추가
- DB 쿼리: CTE + RANK() → 단순 `LIMIT 1` 방식으로 단순화

### 04-api-club-feed-ranking.md (API 6)
- URL: `/server/central/my/feed-rankings?year=` → `/server/central/feeds/ranking?year=&month=`
- `month` 파라미터 추가
- 내 클럽 필터 제거 → 전체 동아리 포함 랭킹으로 변경
- 랭킹 공식 동일하게 수정

### 05-api-club-monthly-best.md (API 7)
- URL: `/server/central/my/feed-rankings/monthly-best` → `/server/central/feeds/status?year=&month=`
- 응답 구조: `myBestFeed + topFeed` → 단순 집계 (`feedCount`, `totalViewCount`, `totalLikeCount`, `totalCommentCount`)
- 피드 없으면 모두 0 반환 (null 아님)

### 06-api-modify-existing.md (API 10~11)
- 댓글 응답 필드: `authorName` → `anonymousName` (`"익명N"` 형식)
- User JOIN 제거 (anonymousNumber로 이름 생성)

## 결정 사항

- **UUID 기반 비회원 인증**: JWT 인증 없이 `X-Anonymous-UUID` 헤더로 식별
- **좋아요 중복 코드 409**: 기존 400 → 409 Conflict (HTTP 시맨틱에 맞게 수정)
- **Caffeine 캐시**: 서버 재시작 시 DB fallback 허용, TTL 14일
- **API 9 단건 반환**: Notion 명세가 월별 목록이 아닌 "절대 1위 단건"임을 확인
- **랭킹 공식 가중치**: view×1 + like×3 + comment×5 (기존 단순 합산과 다름)

## 다음 할 일

- [ ] `feat/DDING-000-feed-like-api` 브랜치에서 API 1~2 구현
- [ ] `feat/DDING-000-feed-comment-api` 브랜치에서 API 3~5 구현
- [ ] `feat/DDING-000-feed-admin-monthly-rank` 브랜치에서 API 8 구현
- [ ] `feat/DDING-000-feed-admin-rank-winners` 브랜치에서 API 9 구현
- [ ] `feat/DDING-000-feed-club-ranking` 브랜치에서 API 6 구현
- [ ] `feat/DDING-000-feed-club-monthly-best` 브랜치에서 API 7 구현
- [ ] `feat/DDING-000-feed-modify-existing` 브랜치에서 API 10~11 구현
