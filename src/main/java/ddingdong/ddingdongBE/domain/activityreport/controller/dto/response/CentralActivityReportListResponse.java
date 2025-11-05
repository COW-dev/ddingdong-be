package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.CentralActivityReportListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record CentralActivityReportListResponse(
        @Schema(description = "동아리 이름", example = "카우")
        String name,

        @Schema(description = "회차", example = "1")
        String term,

        @Schema(description = "활동보고서 정보")
        List<ActivityReportDto> activityReports
) {

    public static CentralActivityReportListResponse from(CentralActivityReportListQuery query) {
        List<ActivityReportDto> activityReports = query.activityReports().stream()
                .map(ActivityReportDto::from)
                .toList();

        return CentralActivityReportListResponse.builder()
                .name(query.name())
                .term(query.term())
                .activityReports(activityReports)
                .build();
    }
}
