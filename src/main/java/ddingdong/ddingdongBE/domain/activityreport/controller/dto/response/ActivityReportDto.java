package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import lombok.Getter;

@Getter
public class ActivityReportDto {
    private Long id;

    public ActivityReportDto (Long id) {
        this.id = id;
    }

    public static ActivityReportDto from(ActivityReport activityReport) {
        return new ActivityReportDto(activityReport.getId());
    }
}
