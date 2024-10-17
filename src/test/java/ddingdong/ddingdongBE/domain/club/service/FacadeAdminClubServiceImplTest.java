package ddingdong.ddingdongBE.domain.club.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import ddingdong.ddingdongBE.domain.scorehistory.entity.Score;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminClubServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminClubService facadeAdminClubService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;

    private final FixtureMonkey fixture = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();
    private CreateClubCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateClubCommand(
                "test",
                "testCategory",
                "testLeaderName",
                "testTag",
                "testtest",
                "abcd1234"
        );
    }

    @DisplayName("어드민: 동아리 생성")
    @Test
    void createClub() {
        // given
        // when
        Long createdClubId = facadeAdminClubService.create(command);

        // then
        Club createdClub = clubRepository.findById(createdClubId).orElse(null);
        Optional<User> createUser = userRepository.findByAuthId(command.authId());

        assertThat(createdClub).isNotNull();
        assertThat(createdClub.getName()).isEqualTo("test");
        assertThat(createdClub.getCategory()).isEqualTo("testCategory");
        assertThat(createdClub.getLeader()).isEqualTo("testLeaderName");
        assertThat(createdClub.getTag()).isEqualTo("testTag");
        assertThat(createUser).isPresent();
    }

    @DisplayName("어드민: 동아리 목록 조회")
    @Test
    void findAllClubs() {
        // given
        List<Club> clubs = fixture.giveMeBuilder(Club.class)
                .set("id", null)
                .set("user", null)
                .set("score", Score.from(BigDecimal.ZERO))
                .set("profileImageKey", "test/file/2024-01-01/test/uuid")
                .set("deletedAt", null)
                .sampleList(3);
        clubRepository.saveAll(clubs);

        // when
        List<AdminClubListQuery> result = facadeAdminClubService.findAll();

        // then
        assertThat(result).hasSize(3);
    }

    @DisplayName("어드민: 동아리 삭제")
    @Test
    void deleteClub() {
        // given
        Long clubId = facadeAdminClubService.create(command);

        // when
        facadeAdminClubService.deleteClub(clubId);

        // then
        Optional<Club> result = clubRepository.findById(clubId);
        assertThat(result).isEmpty();
    }
}
