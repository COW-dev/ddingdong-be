package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;

public record UpdateFixZoneCommentCommand(
        Long fixZoneCommentId,
        String content
) {

    public FixZoneComment toEntity() {
        return FixZoneComment.builder()
                .content(content)
                .build();
    }

}
