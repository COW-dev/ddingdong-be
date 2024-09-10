package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    ClubInformationQuery clubInformationQuery,
    String activityContent,
    String fileUrl,
    String feedType,
    LocalDate createdDate
) {

  public static FeedQuery of(Feed feed, ClubInformationQuery clubInformationQuery) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubInfo(clubInformationQuery)
        .activityContent(feed.getActivityContent())
        .fileUrl(feed.getFileUrl())
        .feedType(feed.getFeedType().toString())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

