package ddingdong.ddingdongBE.domain.fixzone.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
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
class FacadeCentralFixZoneServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeCentralFixZoneService facadeCentralFixZoneService;
    @Autowired
    private FixZoneRepository fixZoneRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("동아리 - 픽스존 생성")
    @Test
    void create() {
        //given
        User user = fixture.giveMeOne(User.class);
        User savedUser = userRepository.save(user);
        Club club = fixture.giveMeBuilder(Club.class)
                .set("user", savedUser)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("deletedAt", null)
                .sample();
        clubRepository.save(club);
        UUID fileId1 = UuidCreator.getTimeOrderedEpoch();
        UUID fileId2 = UuidCreator.getTimeOrderedEpoch();
        CreateFixZoneCommand command = new CreateFixZoneCommand(
                savedUser.getId(),
                "test",
                "test",
                List.of(fileId1.toString(), fileId2.toString())
        );
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId1)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId1)
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.PENDING)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId2)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId2)
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.PENDING)
                        .sample()
        ));

        //when
        Long createdFixZoneId = facadeCentralFixZoneService.create(command);

        //then
        Optional<FixZone> result = fixZoneRepository.findById(createdFixZoneId);
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findByIdIn(List.of(fileId1, fileId2));
        assertThat(result).isPresent();
        assertThat(fileMetaDataList).hasSize(2)
                .extracting("domainType", "entityId", "fileStatus")
                .contains(tuple(DomainType.FIX_ZONE_IMAGE, result.get().getId(), FileStatus.COUPLED));
    }

    @DisplayName("동아리 - 내 픽스존 목록 조회")
    @Test
    void getMyFixZones() {
        //given
        User user = fixture.giveMeOne(User.class);
        User savedUser = userRepository.save(user);
        Club club = fixture.giveMeBuilder(Club.class)
                .set("user", savedUser)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        List<FixZone> fixZones = fixture.giveMeBuilder(FixZone.class)
                .set("club", savedClub)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sampleList(5);
        fixZoneRepository.saveAll(fixZones);

        //when
        List<CentralMyFixZoneListQuery> result = facadeCentralFixZoneService.getMyFixZones(savedUser.getId());

        //then
        assertThat(result).hasSize(5);
    }


    @DisplayName("동아리 - 픽스존 조회")
    @Test
    void getFixZone() {
        //given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("deletedAt", null)
                .set("profileImageKey", "test/file/2024-01-01/uuid")
                .sample();
        Club savedClub = clubRepository.save(club);
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", savedClub)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);
        UUID fileId1 = UuidCreator.getTimeOrderedEpoch();
        UUID fileId2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId1)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId1)
                        .set("domainType", DomainType.FIX_ZONE_IMAGE)
                        .set("entityId", savedFixZone.getId())
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId2)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId2)
                        .set("domainType", DomainType.FIX_ZONE_IMAGE)
                        .set("entityId", savedFixZone.getId())
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        ));

        //when
        CentralFixZoneQuery result = facadeCentralFixZoneService.getFixZone(savedFixZone.getId());

        //then
        assertThat(result.id()).isEqualTo(savedFixZone.getId());
        assertThat(result.imageUrlQueries()).hasSize(2)
                .extracting("id")
                .containsExactlyInAnyOrder(fileId1.toString(), fileId2.toString());
    }

    @DisplayName("동아리 - 픽스존 조회 - 이미지X")
    @Test
    void getFixZoneWithNoneImage() {
        //given
        Club club = fixture.giveMeBuilder(Club.class)
                .set("user", null)
                .set("clubMembers", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("deletedAt", null)
                .set("profileImageKey", "test/file/2024-01-01/uuid")
                .sample();
        Club savedClub = clubRepository.save(club);
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", savedClub)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);

        //when
        CentralFixZoneQuery result = facadeCentralFixZoneService.getFixZone(savedFixZone.getId());

        //then
        assertThat(result.id()).isEqualTo(savedFixZone.getId());
    }

    @DisplayName("동아리 - 픽스존 수정")
    @Test
    void update() {
        //given
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", null)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);
        UpdateFixZoneCommand command = new UpdateFixZoneCommand(
                savedFixZone.getId(),
                "test",
                "test",
                null
        );

        //when
        facadeCentralFixZoneService.update(command);

        //then
        FixZone result = fixZoneRepository.findById(savedFixZone.getId()).orElseThrow();
        assertThat(result.getTitle()).isEqualTo("test");
        assertThat(result.getContent()).isEqualTo("test");
    }

    @DisplayName("동아리 - 픽스존 삭제")
    @Test
    void delete() {
        //given
        FixZone fixZone = fixture.giveMeBuilder(FixZone.class)
                .set("club", null)
                .set("isCompleted", false)
                .set("deletedAt", null)
                .sample();
        FixZone savedFixZone = fixZoneRepository.save(fixZone);
        UUID fileId1 = UuidCreator.getTimeOrderedEpoch();
        UUID fileId2 = UuidCreator.getTimeOrderedEpoch();
        fileMetaDataRepository.saveAll(List.of(
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId1)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId1)
                        .set("domainType", DomainType.FIX_ZONE_IMAGE)
                        .set("entityId", savedFixZone.getId())
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample(),
                fixture.giveMeBuilder(FileMetaData.class)
                        .set("id", fileId2)
                        .set("fileKey", "test/IMAGE/2024-01-01/" + fileId2)
                        .set("domainType", DomainType.FIX_ZONE_IMAGE)
                        .set("entityId", savedFixZone.getId())
                        .set("fileName", "test")
                        .set("fileStatus", FileStatus.COUPLED)
                        .sample()
        ));

        //when
        facadeCentralFixZoneService.delete(savedFixZone.getId());

        //then
        Optional<FixZone> result = fixZoneRepository.findById(savedFixZone.getId());
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findByIdIn(List.of(fileId1, fileId2));
        assertThat(result.isPresent()).isFalse();
        assertThat(fileMetaDataList).isEmpty();
//        assertThat(fileMetaDataList).hasSize(2)
//                .extracting("fileStatus")
//                .containsOnly(FileStatus.DELETED);
    }
}
