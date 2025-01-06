package ddingdong.ddingdongBE.domain.feed.service.dto.command;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import lombok.Builder;

@Builder
public record UpdateFeedCommand(
    Long feedId,
    String activityContent,
    String mediaId,
    String contentType
) {

    public Feed toEntity() {
        return Feed.builder()
            .activityContent(activityContent)
            .feedType(FeedType.findByContentType(contentType))
            .build();
    }
}
