package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    String activityContent,
    String feedType,
    long viewCount,
    LocalDate createdDate,
    FeedFileInfoQuery feedFileInfoQuery,
    ClubProfileQuery clubProfileQuery
    ) {

  public static FeedQuery of(Feed feed, ClubProfileQuery clubProfileQuery, FeedFileInfoQuery feedFileInfoQuery) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubProfileQuery(clubProfileQuery)
        .activityContent(feed.getActivityContent())
        .feedFileInfoQuery(feedFileInfoQuery)
        .feedType(feed.getFeedType().toString())
        .viewCount(feed.getViewCount())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

