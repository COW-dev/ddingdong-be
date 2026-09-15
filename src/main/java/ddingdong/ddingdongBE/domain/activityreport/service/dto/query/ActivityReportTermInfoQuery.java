package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermInfo;
import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermStatus;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ActivityReportTermInfoQuery(
    int term,
    LocalDate startDate,
    LocalDate endDate,
    ActivityReportTermStatus status
) {

  public static ActivityReportTermInfoQuery from(ActivityReportTermInfo termInfo, LocalDate today) {
    return ActivityReportTermInfoQuery.builder()
        .term(termInfo.getTerm())
        .startDate(termInfo.getStartDate())
        .endDate(termInfo.getEndDate())
        .status(termInfo.getStatus(today))
        .build();
  }
}