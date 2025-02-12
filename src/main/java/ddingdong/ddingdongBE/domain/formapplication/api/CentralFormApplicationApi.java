package ddingdong.ddingdongBE.domain.formapplication.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.FormApplicationResponse;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.UpdateFormApplicationStatusRequest;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.MyFormApplicationsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Form - Club", description = "Form API")
@RequestMapping("/server/central")
public interface CentralFormApplicationApi {
    @Operation(summary = "지원자 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "지원자 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = MyFormApplicationsResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/applications")
    MyFormApplicationsResponse getMyFormApplicationPage(
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "지원자 상세 조회 API")
    @ApiResponse(responseCode = "200", description = "지원자 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = FormApplicationResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/applications/{applicationId}")
    FormApplicationResponse getFormApplication(
            @PathVariable("formId") Long formId,
            @PathVariable("applicationId") Long applicationId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "지원자 상태 수정 API")
    @ApiResponse(responseCode = "204", description = "지원자 상태 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PatchMapping("/my/forms/{formId}/applications")
    void updateFormApplicationStatus(
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody UpdateFormApplicationStatusRequest request
    );
}
