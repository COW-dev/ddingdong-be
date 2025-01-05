package ddingdong.ddingdongBE.domain.fixzone.service.dto.command;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;

public record UpdateFixZoneCommand(
        Long fixZoneId,
        String title,
        String content,
        List<ImageInfo> imageInfos
) {

    public FixZone toEntity() {
        return FixZone.builder()
                .title(title)
                .content(content)
                .build();
    }

    public record ImageInfo(
            String imageId,
            int order
    ) {

    }

}
