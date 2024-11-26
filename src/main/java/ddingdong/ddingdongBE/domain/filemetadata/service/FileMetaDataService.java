package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    UUID create(FileMetaData fileMetaData);

    List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId);

    List<FileMetaData> getCoupledAllByEntityId(Long entityId);

    void updateStatusToCoupled(List<String> ids, DomainType domainType, Long entityId);

    void updateStatusToCoupled(String id, DomainType domainType, Long entityId);

    void updateStatusToDelete(DomainType domainType, Long entityId);

    void update(String id, DomainType domainType, Long entityId);

    void update(List<String> ids, DomainType domainType, Long entityId);
}
