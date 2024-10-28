package ddingdong.ddingdongBE.domain.filemetadata.service.dto.command;

import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import java.util.List;
import java.util.UUID;

public record UpdateAllFileMetaDataCommand(
        List<UUID> ids,
        EntityType entityType,
        Long entityId
) {

}
