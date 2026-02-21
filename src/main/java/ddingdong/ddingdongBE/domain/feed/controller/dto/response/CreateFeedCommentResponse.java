package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import lombok.Builder;

@Builder
public record CreateFeedCommentResponse(Long commentId, int anonymousNumber) {

    public static CreateFeedCommentResponse from(CreateFeedCommentQuery query) {
        return CreateFeedCommentResponse.builder()
                .commentId(query.commentId())
                .anonymousNumber(query.anonymousNumber())
                .build();
    }
}
