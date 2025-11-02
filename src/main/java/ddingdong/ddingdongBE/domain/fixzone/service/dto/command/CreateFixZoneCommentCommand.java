package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;
import ddingdong.ddingdongBE.domain.user.entity.User;

public record CreateFixZoneCommentCommand(
        User user,
        Long fixZoneId,
        String content
) {

    public FixZoneComment toEntity(User user, FixZone fixZone) {
        return FixZoneComment.builder()
                .user(user)
                .fixZone(fixZone)
                .content(content)
                .build();
    }

}
