package ddingdong.ddingdongBE.email.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UpdateEmailSendStatusRequest(
        @NotBlank(message = "이벤트 타입은 필수입니다")
        @Schema(description = "이벤트 타입", example = "Bounce", allowableValues = {"Send", "Delivery", "Bounce", "Complaint"})
        String eventType,
        @NotBlank(message = "메시지 ID는 필수입니다")
        @Schema(description = "메시지 ID", example = "0100018d4e8c1234")
        String messageId
) {
}
