package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommentCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "CreateFixZoneCommentRequest", description = "Admin - 픽스존 댓글 등록 요청")
public record CreateFixZoneCommentRequest(
        @Schema(description = "댓글 내용")
        String content
) {

    public CreateFixZoneCommentCommand toCommand(User user, Long fixZoneId) {
        return new CreateFixZoneCommentCommand(user, fixZoneId, content);
    }

}
