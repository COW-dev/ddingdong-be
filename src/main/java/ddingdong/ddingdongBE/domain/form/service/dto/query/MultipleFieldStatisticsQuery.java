package ddingdong.ddingdongBE.domain.form.service.dto.query;

import java.util.List;

public record MultipleFieldStatisticsQuery(
        String type,
        List<OptionStatisticQuery> optionsQueries
) {
    public record OptionStatisticQuery(
            String label,
            int count,
            int ratio
    ) {

    }
}
