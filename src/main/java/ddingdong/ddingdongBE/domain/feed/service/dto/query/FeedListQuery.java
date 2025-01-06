package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import lombok.Builder;

@Builder
public record FeedListQuery(
    Long id,
    String thumbnailUrl,
    String feedType
) {

  public static FeedListQuery from(Feed feed) {
    return FeedListQuery.builder()
        .id(feed.getId())
        .thumbnailUrl(null)
        .feedType(feed.getFeedType().toString())
        .build();
  }
}
