package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.List;
import java.util.UUID;

public interface FileMetaDataService {

    void create(List<FileMetaData> fileMetaDataList);

    void create(FileMetaData fileMetaData);

    FileMetaData getByFileId(UUID fileId);

}
