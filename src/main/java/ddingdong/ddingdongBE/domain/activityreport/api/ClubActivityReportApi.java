package ddingdong.ddingdongBE.domain.activityreport.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequests;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequests;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.AdminActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportTermInfoResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CentralActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Activity Report - Club", description = "Activity Report Club API")
@RequestMapping("/server/central")
public interface ClubActivityReportApi {

    @Operation(summary = "현재 활동보고서 회차 조회")
    @ApiResponse(responseCode = "200", description = "현재 활동보고서 회차 조회 성공",
            content = @Content(schema = @Schema(implementation = CurrentTermResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/activity-reports/current-term")
    CurrentTermResponse getCurrentTerm();

    @Operation(summary = "본인 동아리 활동보고서 전체 조회")
    @ApiResponse(responseCode = "200", description = "본인 동아리 활동보고서 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = AdminActivityReportListResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/activity-reports")
    List<CentralActivityReportListResponse> getMyActivityReports(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "활동보고서 상세 조회")
    @ApiResponse(responseCode = "200", description = "활동보고서 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = ActivityReportResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/activity-reports")
    List<ActivityReportResponse> getActivityReport(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("term") String term
    );

    @Operation(summary = "활동보고서 등록")
    @ApiResponse(responseCode = "201", description = "활동보고서 등록 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping(value = "/my/activity-reports")
    void createActivityReport(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody CreateActivityReportRequests requests
    );

    @Operation(summary = "활동보고서 수정")
    @ApiResponse(responseCode = "200", description = "활동보고서 수정 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping(value = "/my/activity-reports")
    void updateActivityReport(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(value = "term") String term,
            @RequestBody UpdateActivityReportRequests requests
    );

    @Operation(summary = "활동보고서 삭제")
    @ApiResponse(responseCode = "200", description = "활동보고서 삭제 성공")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/my/activity-reports")
    void deleteActivityReport(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam(value = "term") String term
    );

    @Operation(summary = "활동 보고서 회차별 기간 조회 API")
    @ApiResponse(responseCode = "200", description = "활동 보고서 회차별 기간 조회 성공",
            content = @Content(schema = @Schema(implementation = ActivityReportTermInfoResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/activity-reports/term")
    List<ActivityReportTermInfoResponse> getActivityTermInfos();

}
