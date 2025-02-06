package ddingdong.ddingdongBE.domain.formapplication.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.UpdateFormApplicationStatusRequest;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.FormApplicationResponse;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.MyFormApplicationPageResponse;
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
    @Parameter(name = "size", description = "한 페이지당 조회할 지원자 수 (기본값: 15)")
    @Parameter(name = "currentCursorId", description = "현재 커서 위치 (첫 페이지: -1)")
    @ApiResponse(responseCode = "200", description = "지원자 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = MyFormApplicationPageResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}/applications")
    MyFormApplicationPageResponse getMyFormApplicationPage(
            @PathVariable("formId") Long formId,
            @RequestParam(value = "size", defaultValue = "15") int size,
            @RequestParam(value = "currentCursorId", defaultValue = "-1") Long currentCursorId,
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
