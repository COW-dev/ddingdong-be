package ddingdong.ddingdongBE.file.service.dto;

import ddingdong.ddingdongBE.file.entity.FileCategory;
import ddingdong.ddingdongBE.file.entity.FileMetaData;
import lombok.Builder;

@Builder
public record FileMetaDataCommand(
        String fileId,
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
