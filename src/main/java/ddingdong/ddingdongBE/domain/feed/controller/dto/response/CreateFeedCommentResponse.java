package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.CreateFeedCommentQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record CreateFeedCommentResponse(
        @Schema(description = "생성된 댓글 ID", example = "1")
        Long commentId,
        @Schema(description = "익명 번호", example = "3")
        int anonymousNumber
) {

    public static CreateFeedCommentResponse from(CreateFeedCommentQuery query) {
        return CreateFeedCommentResponse.builder()
                .commentId(query.commentId())
                .anonymousNumber(query.anonymousNumber())
                .build();
    }
}
