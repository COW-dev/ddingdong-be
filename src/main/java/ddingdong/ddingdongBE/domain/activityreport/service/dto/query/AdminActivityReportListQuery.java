package ddingdong.ddingdongBE.domain.activityreport.service.dto.query;

import java.util.List;
import lombok.Builder;

@Builder
public record AdminActivityReportListQuery(
    String name,
    List<ActivityReportInfo> activityReports
) {

  public static AdminActivityReportListQuery of(String name, List<ActivityReportInfo> activityReportInfos) {
    return AdminActivityReportListQuery.builder()
        .name(name)
        .activityReports(activityReportInfos)
        .build();
  }
}
