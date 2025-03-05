package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.AdminActivityReportListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminActivityReportListResponse(
        AdminActivityReportListClubResponse club,

        @Schema(description = "활동보고서 정보")
        List<ActivityReportDto> activityReports
) {

    public static AdminActivityReportListResponse from(AdminActivityReportListQuery query) {
        List<ActivityReportDto> activityReports = query.activityReports().stream()
                .map(ActivityReportDto::from)
                .toList();

        return AdminActivityReportListResponse.builder()
                .club(new AdminActivityReportListClubResponse(query.clubId(), query.clubName()))
                .activityReports(activityReports)
                .build();
    }

    public record AdminActivityReportListClubResponse(
            @Schema(description = "동아리 식별자", example = "1")
            Long id,
            @Schema(description = "동아리 이름", example = "카우")
            String name
    ) {

    }
}
