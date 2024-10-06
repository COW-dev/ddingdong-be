package ddingdong.ddingdongBE.domain.question.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Schema(
        name = "AdminQuestionResponse",
        description = "어드민 - FAQ 질문 목록 응답"
)
@Builder
public record AdminQuestionListResponse(

        @Schema(description = "질문 식별자", example = "1")
        Long id,
        @Schema(description = "FAQ 질문", example = "질문")
        String question,
        @Schema(description = "FAQ 답변", example = "답변")
        String reply,
        @Schema(description = "작성일", example = "2024-01-01")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate createdAt
) {

    public static AdminQuestionListResponse from(Question question) {
        return AdminQuestionListResponse.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .reply(question.getReply())
                .createdAt(question.getCreatedAt().toLocalDate())
                .build();
    }
}
