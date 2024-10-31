package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;

public record CreateFixZoneCommand(
        Long userId,
        String title,
        String content,
        List<String> fixZoneImageIds
) {

    public FixZone toEntity(Club club) {
        return FixZone.builder()
                .club(club)
                .title(title)
                .content(content)
                .isCompleted(false)
                .build();
    }

}
