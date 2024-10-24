package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZoneComment;

public record CreateFixZoneCommentCommand(
        Long userId,
        Long fixZoneId,
        String content
) {

    public FixZoneComment toEntity(Club club, FixZone fixZone) {
        return FixZoneComment.builder()
                .club(club)
                .fixZone(fixZone)
                .content(content)
                .build();
    }

}
