package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.SingleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.SingleFieldStatisticsQuery.SingleStatisticsQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SingleFieldStatisticsResponse(
        @Schema(description = "질문 유형", example = "CHECK_BOX")
        String type,
        @ArraySchema(schema = @Schema(implementation = SingleStatisticsResponse.class))
        List<SingleStatisticsResponse> answers
) {

    record SingleStatisticsResponse(
            @Schema(description = "지원자 id", example = "1")
            Long applicationId,
            @Schema(description = "지원자 이름", example = "고선제")
            String name,
            @Schema(description = "지원자 답변", example = "답변입니다")
            String answer
    ) {
        public static SingleStatisticsResponse from(SingleStatisticsQuery query) {
            return new SingleStatisticsResponse(query.applicationId(), query.name(), query.answer());
        }

    }

    public static SingleFieldStatisticsResponse from(SingleFieldStatisticsQuery query) {
        List<SingleStatisticsResponse> answers = query.answers().stream()
                .map(SingleStatisticsResponse::from)
                .toList();
        return new SingleFieldStatisticsResponse(query.type(), answers);
    }
}
