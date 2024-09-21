package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

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

    public static ActivityReportListResponse of(String name, String term, List<ActivityReportDto> activityReportDtos) {
        return ActivityReportListResponse.builder()
            .name(name)
            .term(term)
            .activityReports(activityReportDtos)
            .build();
    }
}
