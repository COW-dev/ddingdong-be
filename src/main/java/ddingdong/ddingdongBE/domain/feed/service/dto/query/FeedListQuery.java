package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedVideoUrlQuery;
import lombok.Builder;

@Builder
public record FeedListQuery(
    Long id,
    String thumbnailCdnUrl,
    String thumbnailOriginUrl,
    String thumbnailFileName,
    String feedType
) {

  public static FeedListQuery of(Feed feed, UploadedVideoUrlQuery urlQuery) {
    return FeedListQuery.builder()
        .id(feed.getId())
        .thumbnailCdnUrl(urlQuery.thumbnailCdnUrl())
        .thumbnailOriginUrl(urlQuery.thumbnailOriginUrl())
        .feedType(feed.getFeedType().toString())
        .build();
  }
}
