package ddingdong.ddingdongBE.domain.formapplicaion.api;

import ddingdong.ddingdongBE.domain.formapplicaion.controller.dto.request.CreateFormResponseRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Form - User", description = "User Form API")
@RequestMapping("/server")
public interface UserFormApi {

    @Operation(summary = "지원하기 API")
    @ApiResponse(responseCode = "201", description = "지원하기 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/forms/{formId}/applications")
    void createFormResponse(@Valid @RequestBody CreateFormResponseRequest request);

}
