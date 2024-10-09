package ddingdong.ddingdongBE.domain.question.controller.dto.request;

import ddingdong.ddingdongBE.domain.question.service.dto.command.CreateQuestionCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "GenerateQuestionRequest",
        description = "FAQ 질문 생성 요청"
)
@Builder
public record CreateQuestionRequest(
        @Schema(description = "FAQ 질문", example = "질문")
        String question,
        @Schema(description = "FAQ 답변", example = "답변")
        String reply
) {

    public CreateQuestionCommand toCommand(User user) {
        return CreateQuestionCommand.builder()
                .admin(user)
                .question(this.question)
                .reply(this.reply).build();
    }
}
