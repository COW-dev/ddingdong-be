package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedQuery(
    Long id,
    ClubInfo clubInfo,
    String activityContent,
    String fileUrl,
    String feedType,
    LocalDate createdDate
) {

  public static FeedQuery of(Feed feed, ClubInfo clubInfo) {
    return FeedQuery.builder()
        .id(feed.getId())
        .clubInfo(clubInfo)
        .activityContent(feed.getActivityContent())
        .fileUrl(feed.getFileUrl())
        .feedType(feed.getFeedType().toString())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

