package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportListQuery(
    String name,
    String term,
    List<ActivityReportInfo> activityReports
) {

  public static ActivityReportListQuery of(String name, String term, List<ActivityReportInfo> activityReportInfos) {
    return ActivityReportListQuery.builder()
        .name(name)
        .term(term)
        .activityReports(activityReportInfos)
        .build();
  }
}
