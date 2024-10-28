package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    void save(List<FileMetaData> fileMetaDataList);

    UUID save(FileMetaData fileMetaData);

    FileMetaData getById(UUID fileId);

    List<FileMetaData> findActivatedAllByEntityTypeAndEntityId(EntityType entityType, Long entityId);

    List<FileMetaData> findAllByEntityTypeAndEntityId(EntityType entityType, Long entityId);

    List<FileMetaData> getByIds(List<UUID> ids);
}
