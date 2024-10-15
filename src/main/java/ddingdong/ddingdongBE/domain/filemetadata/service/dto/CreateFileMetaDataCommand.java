package ddingdong.ddingdongBE.domain.filemetadata.service.dto;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;

public record CreateFileMetaDataCommand(
    String key,
    FileCategory fileCategory
) {

    public FileMetaData toEntity(UUID fileId) {
        return FileMetaData.builder()
            .fileCategory(fileCategory)
            .fileId(fileId)
            .build();
    }
}
