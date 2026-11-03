package ddingdong.ddingdongBE.domain.activityreport.api;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AdminActivityReportListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Activity Report - Admin", description = "Activity Report Admin API")
@RequestMapping("/server/admin/activity-reports")
public interface AdminActivityReportApi {

    @Operation(summary = "어드민 - 활동 보고서 전체 조회")
    @ApiResponse(responseCode = "200", description = "활동 보고서 전체 조회 성공",
        content = @Content(schema = @Schema(implementation = AdminActivityReportListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping()
    List<AdminActivityReportListResponse> getActivityReports(@RequestParam("term") int term);

    @Operation(summary = "어드민 - 활동보고서 상세 조회")
    @ApiResponse(responseCode = "200", description = "활동보고서 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = ActivityReportResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/clubs/{clubId}")
    List<ActivityReportResponse> getActivityReport(
            @PathVariable("clubId") Long clubId,
            @RequestParam("term") int term
    );

    @Operation(summary = "어드민 - 활동 보고서 회차별 기간 설정 API",
            description = "시작일부터 2주 단위로 회차를 생성합니다. 회차 번호는 시작일이 속한 해의 마지막 회차 다음 번호부터 매깁니다.")
    @ApiResponse(responseCode = "201", description = "활동 보고서 회차 생성 성공")
    @ApiResponse(responseCode = "400", description = "시작일이 없거나 회차 수가 1 미만인 경우, 시작일이 기존 회차 마감일 이전이거나 같은 경우, 기존 회차와 기간이 겹치는 경우, 회차 기간이 해를 넘기는 경우")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/term")
    void createActivityTermInfo(@Valid @RequestBody CreateActivityTermInfoRequest request);
}
