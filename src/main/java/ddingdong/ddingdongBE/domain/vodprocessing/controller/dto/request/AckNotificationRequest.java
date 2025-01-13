package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateAckedNotificationCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AckNotificationRequest",
        description = "동아리 - Sse VodProcessingNotification Ack 요청"
)
public record AckNotificationRequest(
        @Schema(description = "수신 완료 VodProcessingNotificaionId", example = "1")
        Long vodNotificationId
) {

    public UpdateAckedNotificationCommand toCommand() {
        return new UpdateAckedNotificationCommand(vodNotificationId);
    }
}
