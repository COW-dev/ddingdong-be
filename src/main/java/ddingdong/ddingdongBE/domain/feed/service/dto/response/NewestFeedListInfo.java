package ddingdong.ddingdongBE.domain.feed.service.dto.response;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record NewestFeedListInfo(
    Long id,
    String thumbnailUrl,
    String feedType
) {

  public static NewestFeedListInfo from(@NonNull Feed feed) {
    return NewestFeedListInfo.builder()
        .id(feed.getId())
        .thumbnailUrl(feed.getThumbnailUrl())
        .feedType(feed.getFeedType().toString())
        .build();
  }
}
