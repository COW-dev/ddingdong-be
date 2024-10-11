package ddingdong.ddingdongBE.domain.filemetadata.service.dto.query;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import java.util.UUID;
import lombok.Builder;

@Builder
public record FileUrlQuery(
    UUID fileId,
    String fileName,
    String fileUrl
) {

    public static FileUrlQuery of(FileMetaData fileMetaData, String fileUrl) {
        return FileUrlQuery.builder()
            .fileId(fileMetaData.getFileId())
            .fileName(fileMetaData.getFileName())
            .fileUrl(fileUrl)
            .build();
    }
}
