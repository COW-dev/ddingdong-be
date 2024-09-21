package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReportTermInfo;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record ActivityReportTermInfoQuery(
    int term,
    LocalDate startDate,
    LocalDate endDate
) {

  public static ActivityReportTermInfoQuery from(ActivityReportTermInfo termInfo) {
    return ActivityReportTermInfoQuery.builder()
        .term(termInfo.getTerm())
        .startDate(termInfo.getStartDate())
        .endDate(termInfo.getEndDate())
        .build();
  }
}
