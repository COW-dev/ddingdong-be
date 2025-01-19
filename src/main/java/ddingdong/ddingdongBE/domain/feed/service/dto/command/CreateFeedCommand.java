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
        try {
            String mediaType = ContentType.getMediaTypeFromMimeType(mimeType);
            return Feed.builder()
                    .activityContent(activityContent)
                    .club(club)
                    .feedType(FeedType.valueOf(mediaType))
                    .build();
        } catch (Exception e) {
            throw new IllegalArgumentException("Feed 내 해당 컨텐츠 종류(" + mimeType + ")는 지원하지 않습니다.");
        }
    }
}
