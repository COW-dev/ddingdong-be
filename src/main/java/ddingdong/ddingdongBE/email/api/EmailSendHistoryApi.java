package ddingdong.ddingdongBE.email.api;

import ddingdong.ddingdongBE.email.controller.dto.UpdateEmailSendStatusRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "EmailSendHistoryApi", description = "EmailSendHistory 관리하는 API")
@RequestMapping("/server")
public interface EmailSendHistoryApi {

    @Operation(summary = "이메일 전송 상태 변경")
    @ApiResponse(responseCode = "200", description = "이메일 전송 상태 변경 성공",
            content = @Content(schema = @Schema(implementation = UpdateEmailSendStatusRequest.class)))
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/internal/email-histories/status")
    void updateEmailSendStatus(@RequestBody @Valid UpdateEmailSendStatusRequest request);
}
