package ddingdong.ddingdongBE.domain.form.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
            @RequestBody CreateFormRequest createFormRequest,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 지원 폼지 수정 API")
    @ApiResponse(responseCode = "204", description = "동아리 지원 폼지 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PutMapping("/my/forms/{formId}")
    void updateForm(
            @RequestBody UpdateFormRequest updateFormRequest,
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );
}
