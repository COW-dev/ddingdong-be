package ddingdong.ddingdongBE.domain.question.controller.dto.request;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "GenerateQuestionRequest",
        description = "FAQ 질문 생성 요청"
)
@Builder
public record GenerateQuestionRequest(
        @Schema(description = "FAQ 질문", example = "질문")
        String question,
        @Schema(description = "FAQ 답변", example = "답변")
        String reply
) {

    public Question toEntity(User user) {
        return Question.builder()
                .user(user)
                .question(this.question)
                .reply(this.reply).build();
    }
}
