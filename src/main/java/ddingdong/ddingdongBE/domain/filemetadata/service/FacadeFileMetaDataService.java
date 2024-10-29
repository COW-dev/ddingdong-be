package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.CreateFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import java.util.List;
import java.util.UUID;

public interface FacadeFileMetaDataService {

    UUID create(CreateFileMetaDataCommand command);

    List<FileMetaDataListQuery> getAllByEntityTypeAndEntityId(DomainType domainType, Long entityId);

    void updateAll(UpdateAllFileMetaDataCommand command);


}
