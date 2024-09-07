package ddingdong.ddingdongBE.domain.feed.service.dto.response;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import lombok.Builder;
import org.springframework.lang.NonNull;

@Builder
public record FeedListInfo(
    Long id,
    String thumbnailUrl,
    String feedType
) {

  public static FeedListInfo from(@NonNull Feed feed) {
    return FeedListInfo.builder()
        .id(feed.getId())
        .thumbnailUrl(feed.getThumbnailUrl())
        .feedType(feed.getFeedType().toString())
        .build();
  }
}
