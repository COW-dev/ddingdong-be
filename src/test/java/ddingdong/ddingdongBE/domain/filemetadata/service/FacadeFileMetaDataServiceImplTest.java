package ddingdong.ddingdongBE.domain.filemetadata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.EntityType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.CreateFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.command.UpdateAllFileMetaDataCommand;
import ddingdong.ddingdongBE.domain.filemetadata.service.dto.query.FileMetaDataListQuery;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeFileMetaDataServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeFileMetaDataService facadeFileMetaDataService;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("FileMetaData 생성")
    @Test
    void create() {
        //given
        UUID id = UuidCreator.getTimeOrderedEpoch();
        CreateFileMetaDataCommand command =
                new CreateFileMetaDataCommand(id, "local/file/2024-01-01/" + id, "test.jpg");

        //when
        UUID createdFileMetaDataId = facadeFileMetaDataService.create(command);

        //then
        Optional<FileMetaData> fileMetaData = fileMetaDataRepository.findById(createdFileMetaDataId);
        assertThat(fileMetaData).isPresent();
    }

    @DisplayName("FileMetaData 조회")
    @Test
    void getAllByEntityTypeAndEntityId() {
        //given
        EntityType entityType = EntityType.CLUB_PROFILE;
        Long entityId = 1L;
        fileMetaDataRepository.saveAll(fixture.giveMeBuilder(FileMetaData.class)
                .set("entityType", entityType)
                .set("entityId", entityId)
                .set("fileStatus", FileStatus.ACTIVATED)
                .sampleList(3));

        //when
        List<FileMetaDataListQuery> result =
                facadeFileMetaDataService.getAllByEntityTypeAndEntityId(entityType, entityId);

        //then
        assertThat(result).hasSize(3);
    }

    @DisplayName("FileMetaData 수정 - Activated")
    @Test
    void updateAllToActivated() {
        //given
        EntityType entityType = EntityType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("entityType", null)
                        .set("entityId", null)
                        .set("fileStatus", FileStatus.PENDING)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("entityType", null)
                        .set("entityId", null)
                        .set("fileStatus", FileStatus.PENDING)
                        .sample()
        ));

        UpdateAllFileMetaDataCommand command = new UpdateAllFileMetaDataCommand(List.of(id1.toString(), id2.toString()),
                entityType, entityId);
        //when
        facadeFileMetaDataService.updateAll(command);

        //then
        List<FileMetaData> result = fileMetaDataRepository.findAllByEntityTypeAndEntityIdWithFileStatus(
                entityType, entityId, FileStatus.ACTIVATED);
        assertThat(result).hasSize(2)
                .extracting("fileStatus")
                .contains(FileStatus.ACTIVATED);
    }

    @DisplayName("FileMetaData 수정 - Activated & Attached")
    @Test
    void updateAllToActivatedAndAttached() {
        //given
        EntityType entityType = EntityType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("entityType", null)
                        .set("entityId", null)
                        .set("fileStatus", FileStatus.PENDING)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("entityType", entityType)
                        .set("entityId", entityId)
                        .set("fileStatus", FileStatus.ACTIVATED)
                        .sample()
        ));

        UpdateAllFileMetaDataCommand command =
                new UpdateAllFileMetaDataCommand(List.of(id1.toString()), entityType, entityId);
        //when
        facadeFileMetaDataService.updateAll(command);

        //then
        List<FileMetaData> result = fileMetaDataRepository.findAllByEntityTypeAndEntityIdWithFileStatus(
                entityType, entityId, FileStatus.ACTIVATED);
        FileMetaData attachedFileMetaData = fileMetaDataRepository.findById(id2).orElseThrow();
        assertThat(result).hasSize(1)
                .extracting("id", "fileStatus")
                .contains(tuple(id1, FileStatus.ACTIVATED));
        assertThat(attachedFileMetaData.getFileStatus()).isEqualTo(FileStatus.ATTACHED);
    }

    @DisplayName("FileMetaData 수정 - Attached")
    @Test
    void updateAllToAttached() {
        //given
        EntityType entityType = EntityType.CLUB_PROFILE;
        Long entityId = 1L;
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id1)
                        .set("entityType", entityType)
                        .set("entityId", entityId)
                        .set("fileStatus", FileStatus.ACTIVATED)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", id2)
                        .set("entityType", entityType)
                        .set("entityId", entityId)
                        .set("fileStatus", FileStatus.ACTIVATED)
                        .sample()
        ));

        UpdateAllFileMetaDataCommand command =
                new UpdateAllFileMetaDataCommand(List.of(), entityType, entityId);
        //when
        facadeFileMetaDataService.updateAll(command);

        //then
        List<FileMetaData> result = fileMetaDataRepository.findByIdIn(List.of(id1, id2));
        assertThat(result).hasSize(2)
                .extracting("fileStatus")
                .contains(FileStatus.ATTACHED);
    }


}
