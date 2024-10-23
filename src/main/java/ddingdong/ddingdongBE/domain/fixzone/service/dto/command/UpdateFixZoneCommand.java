package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;

public record UpdateFixZoneCommand(
        Long fixZoneId,
        String title,
        String content,
        List<String> fixZoneImageKeys
) {

    public FixZone toEntity() {
        return FixZone.builder()
                .title(title)
                .content(content)
                .imageKeys(fixZoneImageKeys)
                .build();
    }

}
