package ddingdong.ddingdongBE.domain.form.controller.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record EmailSendRequestAcceptedResponse(
        @Schema(description = "이메일 전송 기록 ID", example = "123")
        Long formEmailSendHistoryId
) {

    public static EmailSendRequestAcceptedResponse from(Long formEmailSendHistoryId) {
        return new EmailSendRequestAcceptedResponse(formEmailSendHistoryId);
    }
}