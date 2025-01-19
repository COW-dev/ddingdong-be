package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    String activityContent,
    String feedType,
    LocalDate createdDate,
    FeedFileUrlQuery feedFileUrlQuery,
    ClubProfileQuery clubProfileQuery
    ) {

  public static FeedQuery of(Feed feed, ClubProfileQuery clubProfileQuery, FeedFileUrlQuery feedFileUrlQuery) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubProfileQuery(clubProfileQuery)
        .activityContent(feed.getActivityContent())
        .feedFileUrlQuery(feedFileUrlQuery)
        .feedType(feed.getFeedType().toString())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

