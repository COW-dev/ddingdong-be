package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ActivityReportListResponse {
    private String name;

    private String term;

    private List<ActivityReportDto> activityReports;

    @Builder
    public ActivityReportListResponse(String name, String term, List<ActivityReportDto> activityReportDtos) {
        this.name = name;
        this.term = term;
        this.activityReports = activityReportDtos;
    }

    public static ActivityReportListResponse of(String name, String term, List<ActivityReportDto> activityReportDtos) {
        return new ActivityReportListResponse(name, term, activityReportDtos);
    }
}
