package ddingdong.ddingdongBE.domain.feed.controller.dto.request;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommentCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateFeedCommentRequest(
        @NotBlank(message = "댓글 내용은 필수입니다.")
        @Size(max = 500, message = "댓글은 500자 이내로 작성해주세요.")
        String content
) {

    public CreateFeedCommentCommand toCommand(String uuid, Long feedId) {
        return CreateFeedCommentCommand.builder()
                .uuid(uuid)
                .feedId(feedId)
                .content(content)
                .build();
    }
}
