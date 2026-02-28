package ddingdong.ddingdongBE.domain.feed.service.dto.command;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import lombok.Builder;

@Builder
public record CreateFeedCommentCommand(String uuid, Long feedId, String content) {

    public FeedComment toEntity(Feed feed, int anonymousNumber) {
        return FeedComment.builder()
                .feed(feed)
                .uuid(uuid)
                .anonymousNumber(anonymousNumber)
                .content(content)
                .build();
    }
}
