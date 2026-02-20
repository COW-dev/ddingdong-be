# Task 03: Repository 인터페이스 작성

## Overview
FeedLike, FeedComment 엔티티에 대한 Repository 인터페이스를 작성하고, FeedRepository에 랭킹 관련 쿼리 메서드를 추가한다.

## Dependencies
- Task 02 (Entity)

## Files to Create/Modify

### 1. 생성: `domain/feed/repository/FeedLikeRepository.java`

```java
public interface FeedLikeRepository extends JpaRepository<FeedLike, Long> {

    Optional<FeedLike> findByFeedIdAndUserId(Long feedId, Long userId);

    boolean existsByFeedIdAndUserId(Long feedId, Long userId);

    long countByFeedId(Long feedId);

    void deleteByFeedIdAndUserId(Long feedId, Long userId);
}
```

### 2. 생성: `domain/feed/repository/FeedCommentRepository.java`

```java
public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {

    List<FeedComment> findAllByFeedIdOrderByCreatedAtAsc(Long feedId);

    long countByFeedId(Long feedId);
}
```

### 3. 수정: `domain/feed/repository/FeedRepository.java`

랭킹 관련 쿼리 메서드 추가:

```java
// 특정 월의 피드를 club별로 집계하여 랭킹 조회 (네이티브 쿼리)
@Query(value = """
    SELECT c.id as clubId, c.name as clubName,
           COALESCE(SUM(f.view_count), 0) as totalViewCount,
           COALESCE(SUM(comment_counts.cnt), 0) as totalCommentCount,
           COALESCE(SUM(like_counts.cnt), 0) as totalLikeCount
    FROM club c
    LEFT JOIN feed f ON c.id = f.club_id AND f.deleted_at IS NULL
        AND YEAR(f.created_at) = :year AND MONTH(f.created_at) = :month
    LEFT JOIN (
        SELECT feed_id, COUNT(*) as cnt FROM feed_comment WHERE deleted_at IS NULL GROUP BY feed_id
    ) comment_counts ON f.id = comment_counts.feed_id
    LEFT JOIN (
        SELECT feed_id, COUNT(*) as cnt FROM feed_like GROUP BY feed_id
    ) like_counts ON f.id = like_counts.feed_id
    WHERE c.deleted_at IS NULL
    GROUP BY c.id, c.name
    ORDER BY (COALESCE(SUM(f.view_count), 0) + COALESCE(SUM(comment_counts.cnt), 0) * 2 + COALESCE(SUM(like_counts.cnt), 0) * 3) DESC
    """, nativeQuery = true)
List<Object[]> findMonthlyFeedRanking(@Param("year") int year, @Param("month") int month);

// 특정 동아리의 개별 피드 랭킹 (연도 필터, 최신순)
@Query(value = """
    SELECT f.id as feedId, f.view_count as viewCount,
           (SELECT COUNT(*) FROM feed_comment fc WHERE fc.feed_id = f.id AND fc.deleted_at IS NULL) as commentCount,
           (SELECT COUNT(*) FROM feed_like fl WHERE fl.feed_id = f.id) as likeCount
    FROM feed f
    WHERE f.club_id = :clubId AND f.deleted_at IS NULL
        AND YEAR(f.created_at) = :year
    ORDER BY f.created_at DESC
    """, nativeQuery = true)
List<Object[]> findFeedRankingByClubAndYear(@Param("clubId") Long clubId, @Param("year") int year);
```

## Acceptance Criteria
- [ ] FeedLikeRepository: findByFeedIdAndUserId, existsByFeedIdAndUserId, countByFeedId, deleteByFeedIdAndUserId 메서드 존재
- [ ] FeedCommentRepository: findAllByFeedIdOrderByCreatedAtAsc, countByFeedId 메서드 존재
- [ ] FeedRepository에 월별 랭킹 조회 네이티브 쿼리 추가
- [ ] FeedRepository에 동아리별 피드 랭킹 조회 네이티브 쿼리 추가

## Notes
- FeedLike는 hard delete이므로 countByFeedId에서 deleted_at 필터링 불필요
- FeedComment는 soft delete이므로 `@SQLRestriction`에 의해 자동 필터링됨
- 랭킹 총점 공식: viewCount * 1 + commentCount * 2 + likeCount * 3 (가중치는 조정 가능)
- 네이티브 쿼리 결과는 Object[] 대신 interface projection 활용 검토
