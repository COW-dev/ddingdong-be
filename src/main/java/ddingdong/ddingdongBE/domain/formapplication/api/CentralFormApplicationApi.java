package ddingdong.ddingdongBE.domain.formapplication.api;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.formapplication.controller.dto.response.MyFormApplicationPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Form - Club", description = "Form API")
@RequestMapping("/server/central")
public interface CentralFormApplicationApi {
    @Operation(summary = "지원자 전체 조회 API")
    @ApiResponse(responseCode = "200", description = "지원자 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = MyFormApplicationPageResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/my/{formId}/applications")
    MyFormApplicationPageResponse getMyFormApplicationPage(
            @PathVariable("formId") Long formId,
            @RequestParam(value = "size", defaultValue = "15") int size,
            @RequestParam(value = "currentCursorId", defaultValue = "-1") Long currentCursorId,
            @AuthenticationPrincipal PrincipalDetails principalDetails
    );

}
