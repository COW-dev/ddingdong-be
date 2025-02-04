package ddingdong.ddingdongBE.domain.form.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.CreateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.request.UpdateFormRequest;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormListResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormResponse;
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
    List<FormListResponse> getAllMyForm(
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

    @Operation(summary = "동아리 지원 폼지 상세조회 API")
    @ApiResponse(responseCode = "200", description = "동아리 지원 폼지 상세조회 성공",
            content = @Content(schema = @Schema(implementation = FormResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/forms/{formId}")
    FormResponse getForm(
            @PathVariable("formId") Long formId
    );
}
