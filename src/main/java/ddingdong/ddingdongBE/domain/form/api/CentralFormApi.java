package ddingdong.ddingdongBE.domain.form.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.EmailResendApplicationResultRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendStatusOverviewResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendStatusResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.EmailSendApplicationResultRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormEndDateRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.EmailSendCountResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormListResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormStatisticsResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.MultipleFieldStatisticsResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.SingleFieldStatisticsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Form - Club", description = "Form API")
@RequestMapping("/server/central")
public interface CentralFormApi {

    @Operation(summary = "동아리 지원 폼지 생성 API")
    @ApiResponse(responseCode = "201", description = "동아리 지원 폼지 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/my/forms")
    void createForm(
            @Valid @RequestBody CreateFormRequest createFormRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 지원 폼지 수정 API")
    @ApiResponse(responseCode = "204", description = "동아리 지원 폼지 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PutMapping("/my/forms/{formId}")
    void updateForm(
            @Valid @RequestBody UpdateFormRequest updateFormRequest,
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 지원 폼지 삭제 API")
    @ApiResponse(responseCode = "204", description = "동아리 지원 폼지 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/my/forms/{formId}")
    void deleteForm(
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 지원 폼지 전체조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 지원 폼지 전체조회 성공",
            content = @Content(
                    array = @ArraySchema(schema = @Schema(implementation = FormListResponse.class))
            ))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms")
    List<FormListResponse> getAllMyForm(@AuthenticationPrincipal PrincipalDetails principalDetails);

    @Operation(summary = "동아리 지원 폼지 상세조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 지원 폼지 상세조회 성공",
            content = @Content(schema = @Schema(implementation = FormResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}")
    FormResponse getForm(@PathVariable("formId") Long formId);

    @Operation(summary = "동아리 지원 폼지 통계 전체조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 지원 폼지 통계 전체조회 성공",
            content = @Content(schema = @Schema(implementation = FormStatisticsResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/statistics")
    FormStatisticsResponse getFormStatistics(
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 폼지 통계 객관식 상세조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 폼지 통계 객관식 상세조회 성공",
            content = @Content(schema = @Schema(implementation = MultipleFieldStatisticsResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/statistics/multiple-choice")
    MultipleFieldStatisticsResponse getMultipleFieldStatistics(@RequestParam Long fieldId);

    @Operation(summary = "동아리 폼지 통계 주관식 상세조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 폼지 통계 주관식 상세조회 성공",
            content = @Content(schema = @Schema(implementation = SingleFieldStatisticsResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/statistics/text")
    SingleFieldStatisticsResponse getTextFieldStatistics(@RequestParam Long fieldId);

    @Operation(summary = "동아리 최종 합격 지원자 동아리원 명단 등록API")
    @ApiResponse(responseCode = "201", description = "최종 합격 지원자 동아리원 명단 등록 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/my/forms/{formId}/members/register-applicants")
    void registerMembers(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable("formId") Long formId
    );

    @Operation(summary = "동아리 지원 결과 이메일 전송 API")
    @ApiResponse(responseCode = "202", description = "동아리 지원 결과 이메일 전송 성공")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/my/forms/{formId}/results/email")
    void sendApplicationResultEmail(@PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody EmailSendApplicationResultRequest request);

    @Operation(summary = "동아리 폼지 종료일자 수정 API")
    @ApiResponse(responseCode = "204", description = "동아리 폼지 지원기간 마감일자 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping("/my/forms/{formId}/deadline")
    void updateFormEndDate(
            @Valid @RequestBody UpdateFormEndDateRequest updateFormEndDateRequest,
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 폼지 이메일 전송현황 카운트 조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 폼지 이메일 전송현황 카운트 조회 성공",
            content = @Content(schema = @Schema(implementation = EmailSendCountResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/emails/{formEmailSendHistoryId}/counts")
    EmailSendCountResponse getEmailSendCount(
            @PathVariable("formEmailSendHistoryId") Long formEmailSendHistoryId
    );

    @Operation(summary = "이메일 전송 현황 전체조회 API")
    @ApiResponse(responseCode = "200", description = "이메일 전송 현황 전체조회 성공",
            content = @Content(schema = @Schema(implementation = EmailSendStatusOverviewResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/emails/status/overview")
    EmailSendStatusOverviewResponse getEmailSendStatusOverview(
            @PathVariable("formId") Long formId
    );

    @Operation(summary = "이메일 전송 현황 상세조회 API")
    @ApiResponse(responseCode = "200", description = "이메일 전송 현황 상세조회 성공",
            content = @Content(schema = @Schema(implementation = EmailSendStatusResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/emails/status")
    EmailSendStatusResponse getEmailSendStatus(
            @PathVariable("formId") Long formId,
            @RequestParam("status") String status
    );

    @Operation(summary = "동아리 지원 결과 상태별 이메일 재전송 API")
    @ApiResponse(responseCode = "202", description = "동아리 지원 결과 상태별 이메일 재전송 성공")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/my/forms/{formId}/results/email/resends")
    void resendApplicationResultEmail(
            @PathVariable("formId") Long formId,
            @Valid @RequestBody EmailResendApplicationResultRequest request,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
