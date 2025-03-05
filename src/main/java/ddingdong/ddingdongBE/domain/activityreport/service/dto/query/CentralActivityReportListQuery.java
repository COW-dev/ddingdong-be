package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import java.util.List;
import lombok.Builder;

@Builder
public record CentralActivityReportListQuery(
        String name,
        String term,
        List<ActivityReportInfo> activityReports
) {

    public static CentralActivityReportListQuery of(
            String name,
            String term,
            List<ActivityReportInfo> activityReportInfos
    ) {
        return CentralActivityReportListQuery.builder()
                .name(name)
                .term(term)
                .activityReports(activityReportInfos)
                .build();
    }
}
