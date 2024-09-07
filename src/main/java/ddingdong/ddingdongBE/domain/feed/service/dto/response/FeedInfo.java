package ddingdong.ddingdongBE.domain.feed.service.dto.response;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.vo.ClubInfo;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FeedInfo(
    Long id,
    ClubInfo clubInfo,
    String activityContent,
    String fileUrl,
    String feedType,
    LocalDate createdDate
) {

  public static FeedInfo of(Feed feed, ClubInfo clubInfo) {
    return FeedInfo.builder()
        .id(feed.getId())
        .clubInfo(clubInfo)
        .activityContent(feed.getActivityContent())
        .fileUrl(feed.getFileUrl())
        .feedType(feed.getFeedType().toString())
        .createdDate(LocalDate.from(feed.getCreatedAt()))
        .build();
  }
}

