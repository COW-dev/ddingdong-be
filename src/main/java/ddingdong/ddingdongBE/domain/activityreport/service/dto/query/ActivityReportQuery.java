package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import lombok.Builder;

@Builder
public record ActivityReportQuery(
    Long id
) {

  public static ActivityReportQuery from(ActivityReport activityReport) {
    return ActivityReportQuery.builder()
        .id(activityReport.getId())
        .build();
  }
}
