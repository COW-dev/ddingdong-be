package ddingdong.ddingdongBE.domain.filemetadata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class FileMetaDataServiceImplTest extends TestContainerSupport {

    @Autowired
    private FileMetaDataService fileMetaDataService;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;
    @Autowired
    private EntityManager em;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("FileMetaData 생성")
    @Test
    void create() {
        //given
        UUID id = UuidCreator.getTimeOrderedEpoch();
        FileMetaData fileMetaData = FileMetaData.createPending(id, "local/file/2024-01-01/" + id, "test.jpg");

        //when
        UUID createdFileMetaDataId = fileMetaDataService.create(fileMetaData);

        //then
        Optional<FileMetaData> result = fileMetaDataRepository.findById(createdFileMetaDataId);
        assertThat(result).isPresent();
    }

    @DisplayName("FileMetaData 조회")
    @Test
    void getCoupledAllByDomainTypeAndEntityId() {
        //given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        fileMetaDataRepository.saveAll(fixture.giveMeBuilder(FileMetaData.class)
            .set("domainType", domainType)
            .set("entityId", entityId)
            .set("fileStatus", FileStatus.COUPLED)
            .sampleList(3));

        //when
        List<FileMetaData> result =
            fileMetaDataService.getCoupledAllByDomainTypeAndEntityId(domainType, entityId);

        //then
        assertThat(result).hasSize(3);
    }

    @DisplayName("FileMetaData 수정 - COUPLED")
    @Test
    void updateAllToActivated() {
        //given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("domainType", null)
                .set("entityId", null)
                .set("fileStatus", FileStatus.PENDING)
                .sample(),
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id2)
                .set("domainType", null)
                .set("entityId", null)
                .set("fileStatus", FileStatus.PENDING)
                .sample()
        ));

        //when
        fileMetaDataService.updateStatusToCoupled(List.of(id1.toString(), id2.toString()), domainType, entityId);
        em.flush();
        em.clear();
        //then
        List<FileMetaData> result = fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(
            domainType, entityId, FileStatus.COUPLED);
        assertThat(result).hasSize(2)
            .extracting("fileStatus")
            .contains(FileStatus.COUPLED);
    }

    @DisplayName("FileMetaData 수정 - COUPLED & DELETED id를 일부 수정할 경우")
    @Test
    void updateAllToActivatedAndAttached() {
        //given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        UUID id3 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("domainType", domainType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.COUPLED)
                .sample(),
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id2)
                .set("domainType", domainType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.COUPLED)
                .sample()
        ));
        FileMetaData fileMetaData = fixture.giveMeBuilder(FileMetaData.class)
            .set("id", id3)
            .set("domainType", domainType)
            .set("entityId", entityId)
            .set("fileStatus", FileStatus.PENDING)
            .sample();
        fileMetaDataRepository.save(fileMetaData);
        //when
        fileMetaDataService.update(List.of(id1.toString(), id3.toString()), domainType, entityId);
        em.flush();
        //then
        List<FileMetaData> result = fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(
            domainType, entityId, FileStatus.COUPLED);

        Optional<FileMetaData> deletedFileMetaData = fileMetaDataRepository.findById(id2);
        assertThat(result).hasSize(2)
            .extracting("id", "fileStatus")
            .contains(tuple(id1, FileStatus.COUPLED), tuple(id3, FileStatus.COUPLED));
        assertThat(deletedFileMetaData).isPresent();
        assertThat(deletedFileMetaData.get().getFileStatus()).isEqualTo(FileStatus.DELETED);
    }

    @DisplayName("FileMetaData 수정 - COUPLED & DELETED 기존 아이디를 그대로 입력할 경우")
    @Test
    void updateToActivatedAndAttached() {
        //given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.save(
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("domainType", domainType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.COUPLED)
                .sample()
        );

        //when
        fileMetaDataService.update(id1.toString(), domainType, entityId);

        //then
        List<FileMetaData> result = fileMetaDataRepository.findAllByDomainTypeAndEntityIdWithFileStatus(
            domainType, entityId, FileStatus.COUPLED);

        assertThat(result).hasSize(1)
            .extracting("id", "fileStatus")
            .contains(tuple(id1, FileStatus.COUPLED));
    }


    @DisplayName("FileMetaData 수정 - DELETED")
    @Test
    void updateAllToAttached() {
        //given
        DomainType domainType = DomainType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("domainType", domainType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.COUPLED)
                .sample(),
            fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id2)
                .set("domainType", domainType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.COUPLED)
                .sample()
        ));

        //when
        fileMetaDataService.updateStatusToDelete(domainType, entityId);

        //then
        List<FileMetaData> result = fileMetaDataRepository.findByIdIn(List.of(id1, id2));
        assertThat(result).hasSize(2)
                .extracting(FileMetaData::getFileStatus)
                .containsOnly(FileStatus.DELETED);
    }
}
