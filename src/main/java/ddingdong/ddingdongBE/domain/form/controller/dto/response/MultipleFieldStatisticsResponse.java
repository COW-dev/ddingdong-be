package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.MultipleFieldStatisticsQuery.OptionStatisticQuery;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MultipleFieldStatisticsResponse(
        @Schema(description = "질문 유형", example = "CHECK_BOX")
        String type,
        @ArraySchema(schema = @Schema(implementation = MultipleFieldStatisticsQuery.class))
        List<OptionStatisticResponse> options
) {

    record OptionStatisticResponse(
            @Schema(description = "지문", example = "지문 1입니다.")
            String label,
            @Schema(description = "지문 선택 수", example = "10")
            int count,
            @Schema(description = "지원자 수 대비 지문 선택 비율", example = "80")
            int ratio
    ) {

        public static OptionStatisticResponse from(OptionStatisticQuery query) {
            return new OptionStatisticResponse(query.label(), query.count(), query.ratio());
        }
    }

    public static MultipleFieldStatisticsResponse from(MultipleFieldStatisticsQuery query) {
        List<OptionStatisticResponse> responses = query.optionsQueries().stream()
                .map(OptionStatisticResponse::from)
                .toList();
        return new MultipleFieldStatisticsResponse(query.type(), responses);
    }
}
