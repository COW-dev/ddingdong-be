package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import ddingdong.ddingdongBE.domain.form.service.dto.query.TextFieldStatisticsQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.TextFieldStatisticsQuery.TextStatisticsQuery;
import java.util.List;

public record TextFieldStatisticsResponse(
        String type,
        List<TextStatisticsResponse> answers
) {

    record TextStatisticsResponse(
            Long applicationId,
            String name,
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
