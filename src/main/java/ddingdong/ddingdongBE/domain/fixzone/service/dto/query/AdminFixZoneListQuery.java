package ddingdong.ddingdongBE.domain.fixzone.service.dto.query;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.time.LocalDateTime;

public record AdminFixZoneListQuery(
        Long fixZoneId,
        String clubLocation,
        String clubName,
        String title,
        boolean isCompleted,
        LocalDateTime requestedAt
) {

    public static AdminFixZoneListQuery from(FixZone fixZone) {
        return new AdminFixZoneListQuery(
                fixZone.getId(),
                fixZone.getClub().getLocation().getValue(),
                fixZone.getClub().getName(),
                fixZone.getTitle(),
                fixZone.isCompleted(),
                fixZone.getCreatedAt()
        );
    }

}
