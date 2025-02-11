package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.TextFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.TextFieldStatisticsQuery.TextStatisticsQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record TextFieldStatisticsResponse(
        @Schema(description = "질문 유형", example = "CHECK_BOX")
        String type,
        @ArraySchema(schema = @Schema(implementation = TextStatisticsResponse.class))
        List<TextStatisticsResponse> answers
) {

    record TextStatisticsResponse(
            @Schema(description = "지원자 id", example = "1")
            Long applicationId,
            @Schema(description = "지원자 이름", example = "고선제")
            String name,
            @Schema(description = "지원자 답변", example = "답변입니다")
            String answer
    ) {
        public static TextStatisticsResponse from(TextStatisticsQuery query) {
            return new TextStatisticsResponse(query.applicationId(), query.name(), query.answer());
        }

    }

    public static TextFieldStatisticsResponse from(TextFieldStatisticsQuery query) {
        List<TextStatisticsResponse> answers = query.answers().stream()
                .map(TextStatisticsResponse::from)
                .toList();
        return new TextFieldStatisticsResponse(query.type(), answers);
    }
}
