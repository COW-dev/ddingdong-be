package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.domain.ActivityReport;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ActivityReportDto(
    @Schema(description = "활동보고서 ID", example = "1")
    Long id
) {

    public static ActivityReportDto from(ActivityReport activityReport) {
        return ActivityReportDto.builder()
            .id(activityReport.getId())
            .build();
    }
}
