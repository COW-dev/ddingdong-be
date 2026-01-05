package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import java.util.UUID;

public class FileMetaDataFixture {

    public static FileMetaData create(UUID id, Long entityId, DomainType domainType) {
        return FileMetaData.builder()
                .id(id)
                .fileKey("1")
                .fileStatus(FileStatus.COUPLED)
                .fileName("파일")
                .domainType(domainType)
                .entityId(entityId)
                .build();
    }

    public static FileMetaData create(UUID id, Long entityId, DomainType domainType, String filename) {
        return FileMetaData.builder()
                .id(id)
                .fileKey("1")
                .fileStatus(FileStatus.COUPLED)
                .fileName(filename)
                .domainType(domainType)
                .entityId(entityId)
                .build();
    }

    public static FileMetaData createCoupledFileMetaData(UUID id, Long entityId, DomainType domainType,
            String fileKey, String fileName) {
        return FileMetaData.builder()
                .id(id)
                .fileKey(fileKey)
                .fileStatus(FileStatus.COUPLED)
                .fileName(fileName)
                .domainType(domainType)
                .entityId(entityId)
                .build();
    }

    public static FileMetaData createFileMetaDataWithFileStatus(UUID id, FileStatus fileStatus) {
        return FileMetaData.builder()
                .id(id)
                .fileKey("test")
                .fileName("test")
                .fileStatus(fileStatus)
                .build();
    }
}
