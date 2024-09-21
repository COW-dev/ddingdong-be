package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record ActivityReportListResponse(
    @Schema(description = "동아리 이름", example = "카우")
    String name,

    @Schema(description = "회차", example = "1")
    String term,

    @Schema(description = "활동보고서 정보")
    List<ActivityReportDto> activityReports
) {

    public static ActivityReportListResponse from(ActivityReportListQuery query) {
        List<ActivityReportDto> activityReports = query.activityReports().stream()
            .map(ActivityReportDto::from)
            .toList();

        return ActivityReportListResponse.builder()
            .name(query.name())
            .term(query.term())
            .activityReports(activityReports)
            .build();
    }
}
