package ddingdong.ddingdongBE.domain.filemetadata.service.dto.command;

import static ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus.PENDING;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;

public record CreateFileMetaDataCommand(
        UUID id,
        String fileKey,
        String fileName
) {

    public FileMetaData toEntity() {
        return FileMetaData.builder()
                .id(id)
                .fileKey(fileKey)
                .fileName(fileName)
                .fileStatus(PENDING)
                .build();
    }

}
