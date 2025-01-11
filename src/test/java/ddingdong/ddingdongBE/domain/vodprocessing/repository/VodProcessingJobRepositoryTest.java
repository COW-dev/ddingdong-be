package ddingdong.ddingdongBE.domain.vodprocessing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import jakarta.persistence.Persistence;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class VodProcessingJobRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private VodProcessingJobRepository vodProcessingJobRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;


    @DisplayName("FileMetaData의 entityId와 domainType으로 VodProcessingJob을 조회한다")
    @Test
    void findFirstByFileMetaDataEntityIdAndDomainType() {
        // given
        Long entityId = 1L;
        DomainType domainType = DomainType.FEED_VIDEO;

        FileMetaData fileMetaData = FileMetaData.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .fileKey("test-key")
                .fileName("test.mp4")
                .domainType(domainType)
                .entityId(entityId)
                .fileStatus(FileStatus.COUPLED)
                .build();
        fileMetaDataRepository.save(fileMetaData);

        VodProcessingJob vodProcessingJob = VodProcessingJob.builder()
                .fileMetaData(fileMetaData)
                .convertJobId(UuidCreator.getTimeOrderedEpoch().toString())
                .userId("testUser")
                .convertJobStatus(ConvertJobStatus.PENDING)
                .build();
        vodProcessingJobRepository.save(vodProcessingJob);

        // when
        Optional<VodProcessingJob> result = vodProcessingJobRepository
                .findFirstByFileMetaDataEntityIdAndDomainType(entityId, domainType);

        // then
        assertThat(result).isPresent();
        VodProcessingJob foundJob = result.get();
        assertThat(foundJob.getFileMetaData().getEntityId()).isEqualTo(entityId);
        assertThat(foundJob.getFileMetaData().getDomainType()).isEqualTo(domainType);

        // FETCH JOIN 검증
        assertThat(isPersistenceLoaded(foundJob.getFileMetaData())).isTrue();
    }

    @DisplayName("조건에 맞는 데이터가 없으면 빈 Optional을 반환한다")
    @Test
    void findFirstByFileMetaDataEntityIdAndDomainType_ReturnEmptyWhenNotFound() {
        // given
        Long nonExistentEntityId = 999L;
        DomainType domainType = DomainType.FEED_VIDEO;

        // when
        Optional<VodProcessingJob> result = vodProcessingJobRepository
                .findFirstByFileMetaDataEntityIdAndDomainType(nonExistentEntityId, domainType);

        // then
        assertThat(result).isEmpty();
    }

    // Hibernate의 PersistenceUtil을 사용하여 엔티티가 초기화되었는지 확인
    private boolean isPersistenceLoaded(Object entity) {
        return Persistence.getPersistenceUtil().isLoaded(entity);
    }
}
