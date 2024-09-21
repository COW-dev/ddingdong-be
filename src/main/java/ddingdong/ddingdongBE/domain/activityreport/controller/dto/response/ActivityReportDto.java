package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import lombok.Builder;

@Builder
public record ActivityReportDto(
    Long id
) {

    public static ActivityReportDto from(ActivityReport activityReport) {
        return ActivityReportDto.builder()
            .id(activityReport.getId())
            .build();
    }
}
