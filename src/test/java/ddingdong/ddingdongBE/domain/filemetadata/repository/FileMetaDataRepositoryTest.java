package ddingdong.ddingdongBE.domain.filemetadata.repository;

import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FileMetaDataRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void findAllByDomainTypeAndEntityId() {
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long id = 1L;
        FileMetaData fileMetaData = FileMetaData.builder()
            .id(UUID.randomUUID())
            .fileKey("123")
            .fileName("1234.png")
            .domainType(domainType)
            .entityId(id)
            .fileStatus(FileStatus.COUPLED)
            .build();
        fileMetaDataRepository.save(fileMetaData);

        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findAllByDomainTypeAndEntityId(domainType, id);

        Assertions.assertThat(fileMetaDataList.size()).isEqualTo(1);
        Assertions.assertThat(fileMetaDataList.get(0).getFileKey()).isEqualTo("123");
    }

    @DisplayName("여러 개 UUID를 사용하여 fileMetaData를 조회한다.")
    @Test
    void findByIn() {
      // given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        Long entityId2 = 2L;
        UUID id = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        FileMetaData fileMetaData = FileMetaData.builder()
            .id(id)
            .fileKey("123")
            .fileName("1234.png")
            .domainType(domainType)
            .entityId(entityId)
            .fileStatus(FileStatus.COUPLED)
            .build();
        FileMetaData fileMetaData2 = FileMetaData.builder()
            .id(id2)
            .fileKey("12344")
            .fileName("123455.png")
            .domainType(domainType)
            .entityId(entityId2)
            .fileStatus(FileStatus.DELETED)
            .build();
        fileMetaDataRepository.saveAll(List.of(fileMetaData, fileMetaData2));
      // when
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findByIdIn(List.of(id, id2));
        // then
        Assertions.assertThat(fileMetaDataList.size()).isEqualTo(1);
        Assertions.assertThat(fileMetaDataList.get(0).getFileKey()).isEqualTo("123");
    }
}
