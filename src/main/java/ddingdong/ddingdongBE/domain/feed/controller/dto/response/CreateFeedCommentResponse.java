package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.result.CreateFeedCommentResult;
import lombok.Builder;

@Builder
public record CreateFeedCommentResponse(Long commentId, int anonymousNumber) {

    public static CreateFeedCommentResponse from(CreateFeedCommentResult result) {
        return CreateFeedCommentResponse.builder()
                .commentId(result.commentId())
                .anonymousNumber(result.anonymousNumber())
                .build();
    }
}
