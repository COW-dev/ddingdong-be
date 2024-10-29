package ddingdong.ddingdongBE.domain.filemetadata.service.dto.command;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import java.util.List;

public record UpdateAllFileMetaDataCommand(
        List<String> ids,
        DomainType domainType,
        Long entityId
) {

}
