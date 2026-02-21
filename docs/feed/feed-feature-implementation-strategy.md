# 피드 추가기능 구현 전략 (좋아요·댓글·조회수·랭킹)

> 작성일: 2026-02-21
> 브랜치 계획: `.claude/plans/feed-feature/index.md` 참조

---

## 1. 개요

### 사용자 규모 및 환경

| 항목 | 내용 |
|------|------|
| 플랫폼 | 명지대학교 동아리 통합 플랫폼 |
| 인프라 | AWS Elastic Beanstalk, MySQL (RDS), 단일 서버 인스턴스 |
| 인증 방식 | JWT (회원) + UUID 헤더 `X-Anonymous-UUID` (비회원) |
| 서버 재시작 | 배포 시 재시작 발생 — 인메모리 캐시 소실 고려 필요 |

### 목표

- 피드에 **좋아요·댓글·조회수** 상호작용 추가
- **비회원(UUID)** 도 좋아요·댓글 가능
- **랭킹 API** 로 총동연(Admin)·동아리(Club) 에게 월별 피드 현황 제공
- 성능 병목 없이 **N+1 없는** 목록 조회 보장

---

## 2. 기능별 구현 전략

### 2-1. 조회수 (viewCount)

#### 설계 결정: Feed 엔티티 컬럼 직접 관리

```java
// Feed.java
@Column(nullable = false)
private Long viewCount = 0L;

public void incrementViewCount() {
    this.viewCount++;
}
```

| 항목 | 내용 |
|------|------|
| 저장 위치 | `feed.view_count` 컬럼 |
| 증가 시점 | 피드 상세 조회 API 호출 시 |
| 동시성 | 단일 인스턴스 환경 → 별도 처리 불필요 (향후 스케일아웃 시 Redis 원자 카운터 고려) |
| 현황 | Flyway 마이그레이션 완료, `incrementViewCount()` 메서드 구현됨 |

> **⚠️ 현재 이슈**: `FacadeFeedServiceImpl`의 상세 조회 로직에서 `viewCount` 증가 호출이 누락되어 있을 수 있음. 06 plan 적용 시 검증 필요.

---

### 2-2. 좋아요 (likeCount)

#### 설계 결정: UUID 기반 + Hard Delete + Caffeine 캐시

```
[요청 수신]
    ↓
[Caffeine 캐시 확인] → 캐시 히트: 즉시 중복 판정
    ↓ (캐시 미스)
[DB 확인 (fallback)] → 결과를 캐시에 적재
    ↓
[feed_like 테이블 INSERT/DELETE]
```

**Hard Delete 채택 이유**:
- Soft delete(`deleted_at`) + unique constraint `(feed_id, uuid)` 조합 시, 삭제 후 재좋아요 불가 (unique 충돌)
- `feed_like` 테이블만 예외적으로 hard delete 적용

**Caffeine 캐시 (중복 방지)**:

```java
// FeedLikeCacheService.java
Cache<String, Set<Long>> likeCache = Caffeine.newBuilder()
        .expireAfterWrite(14, TimeUnit.DAYS)
        .build();
// Key: uuid (String), Value: 좋아요한 feedId Set
```

| 항목 | 내용 |
|------|------|
| 캐시 TTL | 14일 (클라이언트 UUID 보존 기간 기준) |
| 서버 재시작 시 | 캐시 소실 → DB fallback으로 재적재 |
| 동시성 | `computeIfAbsent` + `Collections.synchronizedSet` 사용 |

**DB 스키마 변경** (Flyway 마이그레이션):

```sql
-- feed_like 테이블: user_id → uuid 컬럼으로 전환
ALTER TABLE feed_like
    DROP FOREIGN KEY fk_feed_like_user,
    DROP COLUMN user_id,
    ADD COLUMN uuid VARCHAR(36) NOT NULL AFTER feed_id;

CREATE UNIQUE INDEX uidx_feed_like_feed_uuid ON feed_like (feed_id, uuid);
```

---

### 2-3. 댓글 (commentCount)

#### 설계 결정: UUID 기반 익명 번호 + Soft Delete

**익명 번호 부여 로직**:
- 피드별로 독립적인 번호 부여
- 동일 피드에서 동일 UUID 재방문 시 기존 번호 재사용
- 신규 UUID: `MAX(anonymous_number) + 1`

```java
int anonymousNumber = feedCommentRepository
        .findAnonymousNumberByFeedIdAndUuid(feedId, uuid)
        .orElseGet(() ->
            feedCommentRepository.findMaxAnonymousNumberByFeedId(feedId) + 1
        );
```

**응답 표현**: `"익명{anonymousNumber}"` (예: `"익명1"`, `"익명3"`)

**삭제 권한**:
| 삭제 주체 | API | 검증 방법 |
|---------|-----|---------|
| 비회원 본인 | `DELETE /server/feeds/{feedId}/comments/{commentId}` | UUID 일치 확인 |
| 동아리 회장 | `DELETE /server/central/feeds/{feedId}/comments/{commentId}` | ROLE_CLUB JWT |

**DB 스키마 변경**:

```sql
-- feed_comment 테이블: user_id → uuid, anonymous_number 컬럼 추가
ALTER TABLE feed_comment
    DROP FOREIGN KEY fk_feed_comment_user,
    DROP COLUMN user_id,
    ADD COLUMN uuid VARCHAR(36) NOT NULL AFTER feed_id,
    ADD COLUMN anonymous_number INT NOT NULL AFTER uuid;
```

---

## 3. 랭킹 집계 전략

### 3-1. 점수 공식

```
score = viewCount × 1 + likeCount × 3 + commentCount × 5
```

| 지표 | 가중치 | 근거 |
|------|--------|------|
| viewCount | ×1 | 가장 빈번하게 발생, 낮은 신호 강도 |
| likeCount | ×3 | 명시적 긍정 반응, 중간 신호 강도 |
| commentCount | ×5 | 가장 적극적인 참여, 높은 신호 강도 |

정렬: `score DESC, feedId ASC` (동점 시 feedId 오름차순)

---

### 3-2. 실시간 쿼리 선택 이유

랭킹 집계 방식으로 **실시간 집계 쿼리**를 선택했다.

| 방식 | 장점 | 단점 |
|------|------|------|
| **실시간 집계** (선택) | 항상 정확, 구현 단순, 스케줄러 불필요 | 쿼리 비용 발생 |
| 사전 집계 (Batch/Scheduler) | 빠른 읽기 | 지연 데이터, 운영 복잡도 증가 |
| Redis Sorted Set | 초고성능 실시간 | 인프라 추가, 관리 복잡도 증가 |

**선택 근거**:
- 명지대 동아리 플랫폼은 대규모 트래픽 환경이 아님
- 랭킹 조회는 Admin·Club 사용자만 (요청 빈도 낮음)
- 정확성이 성능보다 중요
- Redis 등 추가 인프라 없이 단일 MySQL로 운영 가능

---

### 3-3. 필수 인덱스

랭킹 쿼리 성능을 위해 아래 인덱스가 필요하다.

```sql
-- 월별 피드 필터링
ALTER TABLE feed ADD INDEX idx_feed_created_at (created_at);

-- 좋아요 집계 (feed_id 기준 COUNT)
-- feed_like.feed_id는 FK 인덱스로 자동 생성됨

-- 댓글 집계 (soft delete 필터 + feed_id 기준 COUNT)
ALTER TABLE feed_comment ADD INDEX idx_feed_comment_feed_deleted (feed_id, deleted_at);
```

**랭킹 쿼리 구조** (Admin 월별 랭킹 예시):

```sql
SELECT
    f.id              AS feedId,
    f.thumbnail_url   AS thumbnailUrl,
    c.name            AS clubName,
    f.view_count      AS viewCount,
    COUNT(DISTINCT fl.id) AS likeCount,
    COUNT(DISTINCT fc.id) AS commentCount,
    (f.view_count * 1 + COUNT(DISTINCT fl.id) * 3 + COUNT(DISTINCT fc.id) * 5) AS score
FROM feed f
JOIN club c ON f.club_id = c.id
LEFT JOIN feed_like fl ON fl.feed_id = f.id
LEFT JOIN feed_comment fc ON fc.feed_id = f.id AND fc.deleted_at IS NULL
WHERE f.deleted_at IS NULL
  AND YEAR(f.created_at) = :year
  AND MONTH(f.created_at) = :month
GROUP BY f.id, f.thumbnail_url, c.name, f.view_count
ORDER BY score DESC, f.id ASC
```

**동아리용 (CLUB 랭킹)**: CTE + `RANK() OVER(...)` 윈도우 함수로 동점 처리

```sql
WITH global_ranked AS (
    SELECT ...,
        RANK() OVER (
            ORDER BY score DESC, f.id ASC
        ) AS rankPos
    FROM feed f ...
    GROUP BY ...
)
SELECT * FROM global_ranked ORDER BY rankPos ASC
```

---

## 4. N+1 방지 전략 (목록 조회)

피드 목록 API에서 각 피드마다 likeCount·commentCount를 개별 쿼리로 조회하면 **N+1 문제** 발생.

### 해결: feedId IN 절 일괄 조회

```java
// FacadeFeedServiceImpl 목록 조회 흐름
List<Long> feedIds = feeds.stream().map(Feed::getId).toList();

// 1회 쿼리로 모든 카운트 조회
Map<Long, Long> likeCounts    = feedLikeRepository.countByFeedIds(feedIds);
Map<Long, Long> commentCounts = feedCommentRepository.countByFeedIds(feedIds);

// 각 FeedListQuery에 카운트 주입
feeds.stream()
     .map(feed -> FeedListQuery.builder()
             // ...
             .likeCount(likeCounts.getOrDefault(feed.getId(), 0L))
             .commentCount(commentCounts.getOrDefault(feed.getId(), 0L))
             .build())
     .toList();
```

**필요 Repository 메서드**:
```java
// FeedLikeRepository
@Query("SELECT fl.feed.id AS feedId, COUNT(fl) AS cnt FROM FeedLike fl WHERE fl.feed.id IN :feedIds GROUP BY fl.feed.id")
List<FeedCountDto> countByFeedIds(@Param("feedIds") List<Long> feedIds);

// FeedCommentRepository
@Query("SELECT fc.feed.id AS feedId, COUNT(fc) AS cnt FROM FeedComment fc WHERE fc.feed.id IN :feedIds GROUP BY fc.feed.id")
List<FeedCountDto> countByFeedIds(@Param("feedIds") List<Long> feedIds);
```

---

## 5. 전체 흐름도

```
비회원 요청                           회원(CLUB/ADMIN) 요청
     │                                        │
     ▼                                        ▼
X-Anonymous-UUID 헤더 검증            JWT 인증 필터
     │ (UUID v4 정규식)                        │
     ▼                                        ▼
UUID 추출                             User/PrincipalDetails 추출
     │                                        │
     ├──[좋아요 생성/취소]──────────────────────┤
     │   Caffeine 캐시 확인                     │
     │   → 중복이면 409                         │
     │   → 없으면 DB INSERT + 캐시 업데이트       │
     │                                        │
     ├──[댓글 작성]                             │
     │   피드별 익명번호 결정                     │
     │   → 기존 UUID: 재사용                    │
     │   → 신규 UUID: MAX+1                    │
     │   → DB INSERT                           │
     │                                        │
     ├──[피드 상세 조회]                        ├──[랭킹 조회]
     │   viewCount 증가                         │   score = view*1 + like*3 + comment*5
     │   likeCount(DB) 반환                     │   실시간 집계 쿼리
     │   commentCount(DB) 반환                  │   (월별 필터, 인덱스 활용)
     │   comments[] 전체 반환                   │
     │                                        │
     └──[피드 목록 조회]                        │
         feedIds IN 절 일괄 카운트 조회
         N+1 없이 likeCount, commentCount 주입
```

---

## 6. 현재 구현 이슈

| 이슈 | 상태 | 해결 방법 |
|------|------|---------|
| `viewCount` 미증가 | 상세 조회 시 `incrementViewCount()` 호출 누락 가능 | `FacadeFeedServiceImpl` 확인 및 추가 |
| `FeedLike.user_id` 컬럼 | 기존 엔티티가 userId FK로 구현됨 | Flyway 마이그레이션으로 uuid 컬럼으로 전환 |
| `FeedComment.user_id` 컬럼 | 기존 엔티티가 userId FK로 구현됨 | Flyway 마이그레이션으로 uuid, anonymous_number 추가 |
| `thumbnail_url` 컬럼 | Feed 엔티티에 없음 | 랭킹 쿼리에서 사용 → Flyway 추가 또는 파일 서비스로 대체 |
| `likeCount`/`commentCount` 목록 미포함 | 기존 FeedListQuery에 필드 없음 | 06 plan에서 필드 추가 + N+1 방지 로직 적용 |

---

## 7. 결론 — 전략 선택 근거

| 기능 | 선택 전략 | 근거 |
|------|---------|------|
| 좋아요 저장 | Hard Delete + 별도 테이블 | Soft delete + unique constraint 충돌 방지 |
| 좋아요 중복 방지 | Caffeine 인메모리 캐시 | Redis 없이 단일 서버에서 고속 중복 체크 |
| 댓글 삭제 | Soft Delete | 일반 엔티티 일관성 유지 |
| 익명 식별 | UUID v4 헤더 | 로그인 없이 동일 사용자 추적 |
| 조회수 | 엔티티 컬럼 직접 증가 | 단순성, 단일 인스턴스 환경 |
| 랭킹 집계 | 실시간 MySQL 집계 쿼리 | 정확성 우선, 소규모 트래픽 환경 |
| 목록 N+1 방지 | feedIds IN 절 일괄 조회 | 추가 인프라 없이 쿼리 최소화 |

---

## 관련 파일

| 분류 | 경로 |
|------|------|
| 브랜치 계획 | `.claude/plans/feed-feature/index.md` |
| 좋아요 API 계획 | `.claude/plans/feed-feature/00-api-feed-like.md` |
| 댓글 API 계획 | `.claude/plans/feed-feature/01-api-feed-comment.md` |
| Admin 랭킹 계획 | `.claude/plans/feed-feature/02-api-admin-monthly-ranking.md` |
| Admin 지난 1위 계획 | `.claude/plans/feed-feature/03-api-admin-ranking-winners.md` |
| Club 랭킹 계획 | `.claude/plans/feed-feature/04-api-club-feed-ranking.md` |
| Club 이달 현황 계획 | `.claude/plans/feed-feature/05-api-club-monthly-best.md` |
| 기존 API 수정 계획 | `.claude/plans/feed-feature/06-api-modify-existing.md` |
