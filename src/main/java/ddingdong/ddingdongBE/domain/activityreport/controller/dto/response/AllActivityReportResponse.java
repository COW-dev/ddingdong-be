package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AllActivityReportResponse {
    private String name;

    private String term;

    private List<ActivityReportDto> activityReports;

    @Builder
    public AllActivityReportResponse(String name, String term, List<ActivityReportDto> activityReportDtos) {
        this.name = name;
        this.term = term;
        this.activityReports = activityReportDtos;
    }

    public static AllActivityReportResponse of(String name, String term, List<ActivityReportDto> activityReportDtos) {
        return new AllActivityReportResponse(name, term, activityReportDtos);
    }
}
