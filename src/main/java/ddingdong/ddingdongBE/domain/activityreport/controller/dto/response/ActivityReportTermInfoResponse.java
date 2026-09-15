package ddingdong.ddingdongBE.domain.activityreport.controller.dto.response;

import ddingdong.ddingdongBE.domain.activityreport.entity.ActivityReportTermStatus;
import ddingdong.ddingdongBE.domain.activityreport.service.dto.query.ActivityReportTermInfoQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.Builder;

@Schema(
    name = "ActivityReportTermInfoResponse",
    description = "활동 보고서 회차 전체 조회 응답"
)
@Builder
public record ActivityReportTermInfoResponse(
    @Schema(description = "회차", example = "1")
    int term,

    @Schema(description = "시작 일자", example = "2024-07-22")
    LocalDate startDate,

    @Schema(description = "마감 일자", example = "2024-08-04")
    LocalDate endDate,

    @Schema(description = "회차 진행 상태 (UPCOMING: 진행 전, ONGOING: 진행 중, CLOSED: 진행 종료)", example = "ONGOING")
    ActivityReportTermStatus status
) {

    public static ActivityReportTermInfoResponse from(ActivityReportTermInfoQuery query) {
        return ActivityReportTermInfoResponse.builder()
            .term(query.term())
            .startDate(query.startDate())
            .endDate(query.endDate())
            .status(query.status())
            .build();
    }
}
