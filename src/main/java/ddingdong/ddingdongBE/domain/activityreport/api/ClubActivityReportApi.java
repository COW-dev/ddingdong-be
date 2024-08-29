package ddingdong.ddingdongBE.domain.activityreport.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.CreateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.request.UpdateActivityReportRequest;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportListResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.ActivityReportResponse;
import ddingdong.ddingdongBE.domain.activityreport.controller.dto.response.CurrentTermResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Activity Report - Club", description = "Activity Report Club API")
@RequestMapping("/server/club")
public interface ClubActivityReportApi {

    @Operation(summary = "현재 활동보고서 회차 조회")
    @GetMapping("/activity-reports/current-term")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    CurrentTermResponse getCurrentTerm();

    @Operation(summary = "본인 동아리 활동보고서 전체 조회")
    @GetMapping("/my/activity-reports")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<ActivityReportListResponse> getMyActivityReports(
        @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "활동보고서 상세 조회")
    @GetMapping("/activity-reports")
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<ActivityReportResponse> getActivityReport(
        @RequestParam("term") String term,
        @RequestParam("club_name") String clubName
    );

    @Operation(summary = "활동보고서 등록")
    @PostMapping(value = "/my/activity-reports", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void createActivityReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @ModelAttribute(value = "reportData") List<CreateActivityReportRequest> requests,
        @RequestPart(value = "uploadFiles1", required = false) MultipartFile firstImage,
        @RequestPart(value = "uploadFiles2", required = false) MultipartFile secondImage
    );

    @Operation(summary = "활동보고서 수정")
    @PatchMapping(value = "/my/activity-reports", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateActivityReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(value = "term") String term,
        @ModelAttribute(value = "reportData") List<UpdateActivityReportRequest> requests,
        @RequestPart(value = "uploadFiles1", required = false) MultipartFile firstImage,
        @RequestPart(value = "uploadFiles2", required = false) MultipartFile secondImage
    );

    @Operation(summary = "활동보고서 삭제")
    @DeleteMapping("/my/activity-reports")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteActivityReport(
        @AuthenticationPrincipal PrincipalDetails principalDetails,
        @RequestParam(value = "term") String term
    );

}