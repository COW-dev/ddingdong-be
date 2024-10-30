package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    void save(List<FileMetaData> fileMetaDataList);

    UUID save(FileMetaData fileMetaData);

    FileMetaData getById(UUID fileId);

    List<FileMetaData> findActivatedAll(DomainType domainType, Long entityId);

    List<FileMetaData> getByIds(List<UUID> ids);
}
