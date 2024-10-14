package ddingdong.ddingdongBE.domain.filemetadata.service.dto;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileCategory;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FileMetaDataCommand(
    UUID fileId,
    String fileName
) {

    public FileMetaData toEntity(FileCategory fileCategory) {
        return FileMetaData.builder()
            .fileCategory(fileCategory)
            .fileId(fileId)
            .fileName(fileName)
            .build();
    }
}
