package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ActivityReportResponse {
    private String name;

    private String term;

    private List<Long> activityReports;

    @Builder
    public ActivityReportResponse(String name, String term, List<Long> activityReports) {
        this.name = name;
        this.term = term;
        this.activityReports = activityReports;
    }
}
