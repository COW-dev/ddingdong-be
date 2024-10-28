package ddingdong.ddingdongBE.domain.filemetadata.service.dto.query;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;

public record FileMetaDataListQuery(
        UUID id,
        String key,
        String fileName
) {

    public static FileMetaDataListQuery from(FileMetaData fileMetaData) {
        return new FileMetaDataListQuery(fileMetaData.getId(), fileMetaData.getKey(), fileMetaData.getFileName());
    }

}
