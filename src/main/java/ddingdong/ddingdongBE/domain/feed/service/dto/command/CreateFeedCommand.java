package ddingdong.ddingdongBE.domain.feed.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedType;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.file.service.ContentType;
import lombok.Builder;

@Builder
public record CreateFeedCommand(
    String activityContent,
    String mediaId,
    String mimeType,
    User user
) {

    public Feed toEntity(Club club) {
        String mediaType = ContentType.fromMimeType(mimeType).getKeyMediaType();
        return Feed.builder()
            .activityContent(activityContent)
            .club(club)
            .feedType(FeedType.findByContentType(mediaType))
            .build();
    }
}
