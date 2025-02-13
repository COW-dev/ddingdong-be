package ddingdong.ddingdongBE.domain.formapplication.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.request.CreateFormApplicationRequest;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.FormApplicationCountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Form - User", description = "User Form API")
@RequestMapping("/server")
public interface UserFormApplicationApi {

    @Operation(summary = "지원하기 API")
    @ApiResponse(responseCode = "201", description = "지원하기 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/forms/{formId}/applications")
    void createFormApplication(
            @PathVariable Long formId,
            @Valid @RequestBody CreateFormApplicationRequest request
    );

    @Operation(summary = "지원자 수 조회 API")
    @ApiResponse(responseCode = "200", description = "지원자 수 조회 성공",
            content = @Content(schema = @Schema(implementation = FormApplicationCountResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/forms/{formId}/applications/count")
    FormApplicationCountResponse getNumberOfFormApplication(
            @PathVariable("formId") Long formId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}
