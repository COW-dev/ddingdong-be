package ddingdong.ddingdongBE.domain.form.service.dto.query;

import java.util.List;

public record SingleFieldStatisticsQuery(
        String type,
        List<SingleStatisticsQuery> answers
) {

    public record SingleStatisticsQuery(
            Long applicationId,
            String name,
            String answer
    ) {
    }
}
