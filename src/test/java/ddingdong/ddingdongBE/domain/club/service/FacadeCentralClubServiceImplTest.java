package ddingdong.ddingdongBE.domain.club.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.entity.Location;
import ddingdong.ddingdongBE.domain.club.entity.PhoneNumber;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeCentralClubServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeCentralClubService facadeCentralClubService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("중앙동아리: 내 동아리 정보 조회")
    @Test
    void getMyClubInfo() {
        //given
        User savedUser = userRepository.save(fixture.giveMeOne(User.class));
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", 1L)
                .set("user", savedUser)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
                .set("location", Location.from("S1111"))
                .set("profileImageKey", "test/file/2024-01-01/test/uuid")
                .set("introductionImageKey", "test/file/2024-01-01/test/uuid")
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        UUID id1 = UuidCreator.getTimeOrderedEpoch();
        UUID id2 = UuidCreator.getTimeOrderedEpoch();
        FileMetaData clubProfileImageFileMetaData = fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id1)
                .set("fileKey", "test/IMAGE/2024-01-01/" + id1)
                .set("entityType", DomainType.CLUB_PROFILE)
                .set("entityId", savedClub.getId())
                .set("fileStatus", FileStatus.ACTIVATED)
                .sample();
        FileMetaData clubIntroductionImageFileMetaData = fixture.giveMeBuilder(FileMetaData.class)
                .set("id", id2)
                .set("fileKey", "test/IMAGE/2024-01-01/" + id2)
                .set("entityType", DomainType.CLUB_INTRODUCTION)
                .set("entityId", savedClub.getId())
                .set("fileStatus", FileStatus.ACTIVATED)
                .sample();
        fileMetaDataRepository.saveAll(List.of(clubProfileImageFileMetaData, clubIntroductionImageFileMetaData));

        //when
        MyClubInfoQuery result = facadeCentralClubService.getMyClubInfo(savedUser.getId());

        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("중앙동아리: 동아리 정보 수정")
    @Test
    void updateClubInfo() {
        //given
        User savedUser = userRepository.save(fixture.giveMeOne(User.class));
        Club club = fixture.giveMeBuilder(Club.class)
                .set("id", 1L)
                .set("user", savedUser)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("phoneNumber", PhoneNumber.from("010-1234-5678"))
                .set("location", Location.from("S1111"))
                .set("clubMembers", null)
                .set("deletedAt", null)
                .sample();
        Club savedClub = clubRepository.save(club);
        UpdateClubInfoCommand command = new UpdateClubInfoCommand(
                savedUser.getId(),
                "testname",
                "testcategory",
                "testtag",
                "testclubLeader",
                "010-1234-5678",
                "S1111",
                "2024-01-01 01:00",
                "2024-01-01 01:00",
                "testregularMeeting",
                "testintroduction",
                "testactivity",
                "testideal",
                "testformUrl",
                UuidCreator.getTimeBased().toString(),
                UuidCreator.getTimeBased().toString()
        );

        //when
        facadeCentralClubService.updateClubInfo(command);

        //then
        Club result = clubRepository.findById(savedClub.getId()).orElseThrow();
        assertThat(result.getName()).isEqualTo("testname");
    }

}
