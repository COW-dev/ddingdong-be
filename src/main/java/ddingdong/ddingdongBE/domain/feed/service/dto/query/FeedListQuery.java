package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import lombok.Builder;

@Builder
public record FeedListQuery(
        Long id,
        FeedFileInfoQuery thumbnailInfo,
        String feedType,
        long viewCount,
        long likeCount,
        long commentCount
) {

    public static FeedListQuery of(Feed feed, FeedFileInfoQuery thumbnailInfo, long commentCount) {
        return FeedListQuery.builder()
                .id(feed.getId())
                .thumbnailInfo(thumbnailInfo)
                .feedType(feed.getFeedType().name())
                .viewCount(feed.getViewCount())
                .likeCount(feed.getLikeCount())
                .commentCount(commentCount)
                .build();
    }
}
