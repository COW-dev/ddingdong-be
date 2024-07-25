package ddingdong.ddingdongBE.domain.question.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
        name = "ModifyQuestionRequest",
        description = "FAQ 질문 수정 요청"
)
@Builder
public record ModifyQuestionRequest(
        @Schema(description = "자료 제목", example = "제목")
        String question,
        @Schema(description = "자료 내용", example = "내용")
        String reply
) {

    public Question toEntity() {
        return Question.builder()
                .question(this.question)
                .reply(this.reply)
                .build();
    }
}
