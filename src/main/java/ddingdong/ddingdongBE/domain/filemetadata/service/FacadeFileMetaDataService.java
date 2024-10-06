package ddingdong.ddingdongBE.domain.filemetadata.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;

public interface FacadeFileMetaDataService {

    FileMetaData getFileUrlWithMetaData(UUID fileId);


}
