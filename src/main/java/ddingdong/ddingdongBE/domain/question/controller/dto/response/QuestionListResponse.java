package ddingdong.ddingdongBE.domain.question.controller.dto.response;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.service.dto.query.UserQuestionListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "QuestionResponse",
        description = "유저 - FAQ 질문 목록 응답"
)
@Builder
public record QuestionListResponse(

        @Schema(description = "질문 식별자", example = "1")
        Long id,
        @Schema(description = "FAQ 질문", example = "질문")
        String question,
        @Schema(description = "FAQ 답변", example = "답변")
        String reply
) {

    public static QuestionListResponse from(UserQuestionListQuery query) {
        return QuestionListResponse.builder()
                .id(query.id())
                .question(query.question())
                .reply(query.reply())
                .build();
    }
}
