package ddingdong.ddingdongBE.domain.form.api;

import ddingdong.ddingdongBE.domain.form.controller.dto.response.FormSectionResponse;
import ddingdong.ddingdongBE.domain.form.controller.dto.response.UserFormResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Form - User", description = "User Form API")
@RequestMapping("/server")
public interface UserFormApi {

    @Operation(summary = "폼지 섹션 조회 API")
    @ApiResponse(responseCode = "200", description = "폼지 섹션 조회 성공",
            content = @Content(schema = @Schema(implementation = FormSectionResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/forms/{formId}/sections")
    FormSectionResponse getFormSections(
            @PathVariable("formId") Long formId
    );

    @Operation(summary = "폼지 상세 조회 API")
    @ApiResponse(responseCode = "200", description = "폼지 상세 조회 성공",
            content = @Content(schema = @Schema(implementation = UserFormResponse.class)))
    @GetMapping("/forms/{formId}")
    UserFormResponse getForm(
            @PathVariable("formId") Long formId,
            @RequestParam(value = "section") String section
    );
}
