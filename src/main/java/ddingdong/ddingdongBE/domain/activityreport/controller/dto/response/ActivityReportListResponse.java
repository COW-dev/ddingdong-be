package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportListResponse(
    String name,
    String term,
    List<ActivityReportDto> activityReports
) {

    public static ActivityReportListResponse of(String name, String term, List<ActivityReportDto> activityReportDtos) {
        return ActivityReportListResponse.builder()
            .name(name)
            .term(term)
            .activityReports(activityReportDtos)
            .build();
    }
}
