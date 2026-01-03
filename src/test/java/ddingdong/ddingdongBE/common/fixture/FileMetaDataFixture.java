package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import java.util.UUID;

public class FileMetaDataFixture {

    public static FileMetaData create(UUID id, Long entityId) {
        return FileMetaData.builder()
                .id(id)
                .fileKey("1")
                .fileStatus(FileStatus.COUPLED)
                .fileName("파일")
                .domainType(DomainType.FORM_FILE)
                .entityId(entityId)
                .build();
    }

    public static FileMetaData create(UUID id, Long entityId, String filename) {
        return FileMetaData.builder()
                .id(id)
                .fileKey("1")
                .fileStatus(FileStatus.COUPLED)
                .fileName(filename)
                .domainType(DomainType.FORM_FILE)
                .entityId(entityId)
                .build();
    }
}
