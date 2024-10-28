package ddingdong.ddingdongBE.domain.filemetadata.service.dto.command;

import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import java.util.List;

public record UpdateAllFileMetaDataCommand(
        List<String> ids,
        EntityType entityType,
        Long entityId
) {

}
