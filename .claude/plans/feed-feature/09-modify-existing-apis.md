# Task 09: 기존 피드 조회 API 수정

## Overview
기존 피드 전체 목록 조회, 피드 상세 조회, 동아리별 피드 전체 조회 API의 응답에 좋아요 수, 댓글 수, 댓글 목록을 추가한다.

## Dependencies
- Task 04 (Like/Comment Service)
- Task 06 (Like/Comment API)

## Files to Modify

### 1. 피드 전체 목록 조회 (`GET /server/feeds`)

**수정 파일:**
- `domain/feed/service/dto/query/FeedListQuery.java` - likeCount, commentCount 필드 추가
- `domain/feed/controller/dto/response/FeedPageResponse.java` - FeedListResponse에 likeCount, commentCount 추가
- `domain/feed/service/FacadeFeedService.java` - getAllFeedPage()에서 좋아요/댓글 수 조회 로직 추가

FeedListQuery 변경:
```java
public record FeedListQuery(
    Long id,
    String thumbnailCdnUrl,
    String thumbnailOriginUrl,
    String feedType,
    String thumbnailFileName,
    long likeCount,    // 추가
    long commentCount  // 추가
) { ... }
```

FeedPageResponse.FeedListResponse 변경:
```java
@Builder
public record FeedListResponse(
    Long id,
    String thumbnailCdnUrl,
    String thumbnailOriginUrl,
    String thumbnailFilename,
    String feedType,
    long likeCount,    // 추가
    long commentCount  // 추가
) { ... }
```

### 2. 피드 상세 조회 (`GET /server/feeds/{feedId}`)

**수정 파일:**
- `domain/feed/service/dto/query/FeedQuery.java` - likeCount, commentCount, comments 필드 추가
- `domain/feed/controller/dto/response/FeedResponse.java` - likeCount, commentCount, comments 추가
- `domain/feed/service/FacadeFeedService.java` - getById()에서 좋아요/댓글 정보 추가, viewCount 증가 로직

FeedQuery 변경:
```java
public record FeedQuery(
    Long id,
    String activityContent,
    String feedType,
    LocalDate createdDate,
    FeedFileInfoQuery feedFileInfoQuery,
    ClubProfileQuery clubProfileQuery,
    long likeCount,                    // 추가
    long commentCount,                 // 추가
    List<FeedCommentQuery> comments    // 추가
) { ... }
```

FeedResponse 변경 - 댓글 목록용 inner record 추가:
```java
@Builder
record CommentResponse(
    Long id,
    String content,
    String authorName,
    LocalDateTime createdAt
) { ... }
```

신규 DTO:
- `domain/feed/service/dto/query/FeedCommentQuery.java`
```java
public record FeedCommentQuery(
    Long id,
    String content,
    String authorName,
    LocalDateTime createdAt
) {
    public static FeedCommentQuery from(FeedComment comment) { ... }
}
```

### 3. 동아리별 피드 전체 조회 (`GET /server/clubs/{clubId}/feeds`)

**수정 파일:**
- `domain/feed/controller/dto/response/ClubFeedPageResponse.java` - ClubFeedListResponse에 likeCount, commentCount 추가
- `domain/feed/service/FacadeFeedService.java` - getFeedPageByClub()에서 좋아요/댓글 수 조회 추가

### 4. 내 피드 목록 조회 (`GET /server/central/my/feeds`)

**수정 파일:**
- `domain/feed/controller/dto/response/MyFeedPageResponse.java` - likeCount, commentCount 추가
- `domain/feed/service/FacadeClubFeedServiceImpl.java` - getMyFeedPage()에서 좋아요/댓글 수 추가

### 5. FacadeFeedService 수정

```java
// getById() 수정 - viewCount 증가 + 좋아요/댓글 정보 추가
@Transactional  // readOnly = false로 변경 (viewCount 증가)
public FeedQuery getById(Long feedId) {
    Feed feed = feedService.getById(feedId);
    feed.incrementViewCount();  // 조회수 증가

    ClubProfileQuery clubProfileQuery = feedFileService.extractClubInfo(feed.getClub());
    FeedFileInfoQuery feedFileInfoQuery = feedFileService.extractFeedFileInfo(feed);

    long likeCount = feedLikeService.countByFeedId(feedId);
    long commentCount = feedCommentService.countByFeedId(feedId);
    List<FeedComment> comments = feedCommentService.getAllByFeedId(feedId);
    List<FeedCommentQuery> commentQueries = comments.stream()
            .map(FeedCommentQuery::from)
            .toList();

    return FeedQuery.of(feed, clubProfileQuery, feedFileInfoQuery, likeCount, commentCount, commentQueries);
}
```

## Acceptance Criteria
- [ ] 피드 전체 목록 응답에 likeCount, commentCount 포함
- [ ] 피드 상세 조회 응답에 likeCount, commentCount, comments(댓글 목록) 포함
- [ ] 동아리별 피드 조회 응답에 likeCount, commentCount 포함
- [ ] 내 피드 목록 조회 응답에 likeCount, commentCount 포함
- [ ] 피드 상세 조회 시 viewCount 1 증가
- [ ] 기존 필드는 그대로 유지 (하위 호환성)

## Notes
- FacadeFeedService에 FeedLikeService, FeedCommentService 의존성 주입 필요
- FeedListQuery의 기존 of() 팩터리 메서드 시그니처 변경 -> FeedFileService.extractFeedThumbnailInfo() 도 수정 필요
- N+1 문제 주의: 목록 조회 시 각 피드별 좋아요/댓글 수를 개별 쿼리 대신 batch 쿼리로 처리 검토
- viewCount 증가는 동시성 이슈 가능 -> 정확한 실시간 집계가 아니라도 OK라면 현재 방식으로 충분
