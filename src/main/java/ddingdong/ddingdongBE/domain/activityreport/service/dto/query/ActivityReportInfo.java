package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import lombok.Builder;

@Builder
public record ActivityReportInfo(
    Long id
) {

  public static ActivityReportInfo from(ActivityReport activityReport) {
    return ActivityReportInfo.builder()
        .id(activityReport.getId())
        .build();
  }
}
