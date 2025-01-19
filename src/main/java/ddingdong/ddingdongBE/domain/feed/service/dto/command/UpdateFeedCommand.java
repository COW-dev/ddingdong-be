package ddingdong.ddingdongBE.domain.feed.service.dto.command;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import lombok.Builder;

@Builder
public record UpdateFeedCommand(
    Long feedId,
    String activityContent
) {

    public Feed toEntity() {
        return Feed.builder()
            .activityContent(activityContent)
            .build();
    }
}
