package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    String activityContent,
    String feedType,
    long viewCount,
    LocalDate createdDate,
    FeedFileInfoQuery feedFileInfoQuery,
    ClubProfileQuery clubProfileQuery,
    long likeCount,
    long commentCount,
    List<FeedCommentQuery> comments
    ) {

  public static FeedQuery of(Feed feed, ClubProfileQuery clubProfileQuery,
          FeedFileInfoQuery feedFileInfoQuery, long likeCount, long commentCount,
          List<FeedCommentQuery> comments) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubProfileQuery(clubProfileQuery)
        .activityContent(feed.getActivityContent())
        .feedFileInfoQuery(feedFileInfoQuery)
        .feedType(feed.getFeedType().toString())
        .viewCount(feed.getViewCount())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .likeCount(likeCount)
        .commentCount(commentCount)
        .comments(comments)
        .build();
  }
}

