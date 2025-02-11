package ddingdong.ddingdongBE.domain.form.service.dto.query;

import java.util.List;

public record TextFieldStatisticsQuery(
        String type,
        List<TextStatisticsQuery> answers
) {

    public record TextStatisticsQuery(
            Long applicationId,
            String name,
            String answer
    ) {
    }
}
