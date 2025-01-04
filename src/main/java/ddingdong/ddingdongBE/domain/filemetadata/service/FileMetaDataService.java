package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.FileMetaDataIdOrderDto;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    UUID create(FileMetaData fileMetaData);

    List<FileMetaData> getCoupledAllByDomainTypeAndEntityId(DomainType domainType, Long entityId);

    List<FileMetaData> getCoupledAllByEntityId(Long entityId);

    List<FileMetaData> getCoupledAllByDomainTypeAndEntityIdOrderedAsc(DomainType domainType,
                                                                      Long entityId);

    List<FileMetaData> getCoupledAllByEntityIds(List<Long> entityIds);

    void updateStatusToCoupled(List<String> ids, DomainType domainType, Long entityId);

    void updateStatusToCoupled(String id, DomainType domainType, Long entityId);

    void updateStatusToCoupledWithOrder(List<FileMetaDataIdOrderDto> ids, DomainType domainType, Long entityId);

    void updateStatusToDelete(DomainType domainType, Long entityId);

    void update(String id, DomainType domainType, Long entityId);

    void update(List<String> ids, DomainType domainType, Long entityId);

    void updateWithOrder(List<FileMetaDataIdOrderDto> fileMetaDataIdOrderDtos, DomainType domainType, Long entityId);
}
