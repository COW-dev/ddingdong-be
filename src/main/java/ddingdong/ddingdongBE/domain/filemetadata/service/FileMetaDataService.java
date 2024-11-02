package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    UUID create(FileMetaData fileMetaData);

    List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId);

    void updateToCoupled(List<String> ids, DomainType domainType, Long entityId);

    void updateToCoupled(String id, DomainType domainType, Long entityId);

    void updateToDelete(DomainType domainType, Long entityId);

    void update(String id, DomainType domainType, Long entityId);

    void update(List<String> ids, DomainType domainType, Long entityId);
}
