package ddingdong.ddingdongBE.domain.fixzone.service.dto.query;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.time.LocalDateTime;
import java.util.List;

public record CentralFixZoneQuery(
        Long id,
        String clubLocation,
        String clubName,
        String title,
        String content,
        boolean isCompleted,
        LocalDateTime requestedAt,
        List<String> imageUrls
) {

    public static CentralFixZoneQuery of(FixZone fixZone, List<String> imageUrls) {
        return new CentralFixZoneQuery(
                fixZone.getId(),
                fixZone.getClub().getLocation().getValue(),
                fixZone.getClub().getName(),
                fixZone.getTitle(),
                fixZone.getContent(),
                fixZone.isCompleted(),
                fixZone.getCreatedAt(),
                imageUrls
        );
    }

}
