package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import lombok.Getter;

@Getter
public class UpdateActivityReportResponse {
    private Long id;

    public UpdateActivityReportResponse(Long id) {
        this.id = id;
    }

    public static UpdateActivityReportResponse from(ActivityReport activityReport) {
        return new UpdateActivityReportResponse(activityReport.getId());
    }
}
