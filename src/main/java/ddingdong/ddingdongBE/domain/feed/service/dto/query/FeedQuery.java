package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    ClubProfileQuery clubProfileQuery,
    String activityContent,
    String fileUrl,
    String feedType,
    LocalDate createdDate
) {

  public static FeedQuery of(Feed feed, ClubProfileQuery clubProfileQuery) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubProfileQuery(clubProfileQuery)
        .activityContent(feed.getActivityContent())
        .fileUrl(null)
        .feedType(feed.getFeedType().toString())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

