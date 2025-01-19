package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommentCommand;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UpdateFixZoneCommentRequest", description = "Admin - 픽스존 댓글 수정 요청")
public record UpdateFixZoneCommentRequest(
        @Schema(description = "수정할 댓글 내용")
        String content
) {

    public UpdateFixZoneCommentCommand toCommand(Long fixZoneCommentId) {
        return new UpdateFixZoneCommentCommand(fixZoneCommentId, content);
    }
}
