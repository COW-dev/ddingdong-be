package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.FeedComment;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record FeedCommentQuery(
        Long id,
        String uuid,
        String content,
        String anonymousName,
        LocalDateTime createdAt
) {

    public static FeedCommentQuery from(FeedComment comment) {
        return FeedCommentQuery.builder()
                .id(comment.getId())
                .uuid(comment.getUuid())
                .content(comment.getContent())
                .anonymousName("익명" + comment.getAnonymousNumber())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
