package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record FeedListQuery(
        Long id,
        String thumbnailCdnUrl,
        String thumbnailOriginUrl,
        String feedType,
        String thumbnailFileName,
        long viewCount,
        long likeCount,
        long commentCount
) {

}
