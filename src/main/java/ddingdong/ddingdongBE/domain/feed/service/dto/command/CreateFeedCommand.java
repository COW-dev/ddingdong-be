package ddingdong.ddingdongBE.domain.feed.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CreateFeedCommand(
    String activityContent,
    String mediaId,
    User user
) {

    public Feed toEntity(Club club) {
        return Feed.builder()
            .activityContent(activityContent)
            .club(club)
            .build();
    }
}
