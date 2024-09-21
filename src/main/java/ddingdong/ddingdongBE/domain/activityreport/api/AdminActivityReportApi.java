package ddingdong.ddingdongBE.domain.activityreport.api;

import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityTermInfoRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Activity Report - Admin", description = "Activity Report Admin API")
@RequestMapping("/server/admin/activity-reports")
public interface AdminActivityReportApi {

    @Operation(summary = "활동 보고서 전체 조회")
    @ApiResponse(responseCode = "200", description = "활동 보고서 전체 조회 성공",
        content = @Content(schema = @Schema(implementation = ActivityReportListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    List<ActivityReportListResponse> getActivityReports();

    @Operation(summary = "활동 보고서 회차별 기간 설정 API")
    @ApiResponse(responseCode = "200", description = "활동 보고서 전체 조회 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/term")
    void createActivityTermInfo(@RequestBody CreateActivityTermInfoRequest request);
}
