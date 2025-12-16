package ddingdong.ddingdongBE.domain.club.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.command.CreateClubCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.AdminClubListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.ArrayList;
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
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacadeAdminClubService facadeAdminClubService;

    private CreateClubCommand command;

    @BeforeEach
    void setUp() {
        command = new CreateClubCommand(
                "testName",
                "testCategory",
                "testLeaderName",
                "testTag",
                "testAuthId",
                "test1234"
        );
    }

    @DisplayName("어드민: 동아리 생성")
    @Test
    void create() {
        // given
        // when
        Long clubId = facadeAdminClubService.create(command);

        Optional<Club> testClub = clubRepository.findById(clubId);
        Optional<User> testUser = userRepository.findByAuthId(command.authId());

        // then
        assertThat(testClub).isPresent();
        assertThat(testClub.get().getName()).isEqualTo(command.clubName());
        assertThat(testClub.get().getCategory()).isEqualTo(command.category());
        assertThat(testClub.get().getLeader()).isEqualTo(command.leaderName());
        assertThat(testClub.get().getTag()).isEqualTo(command.tag());

        assertThat(testUser).isPresent();
    }

    @DisplayName("어드민: 동아리 목록 조회")
    @Test
    void findAll() {
        // given
        List<Club> clubs = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            Club club = ClubFixture.createClub();
            clubs.add(club);
        }
        clubRepository.saveAll(clubs);

        // when
        List<AdminClubListQuery> result = facadeAdminClubService.findAll();

        // then
        assertThat(result).hasSize(5);
        assertThat(result.get(0).id()).isEqualTo(clubs.get(0).getId());
        assertThat(result.get(0).name()).isEqualTo(clubs.get(0).getName());
    }

    @DisplayName("어드민: 동아리 삭제")
    @Test
    void deleteClub() {
        // given
        Long clubId = facadeAdminClubService.create(command);

        // when
        facadeAdminClubService.deleteClub(clubId);
        Optional<Club> result = clubRepository.findById(clubId);

        // then
        assertThat(result).isEmpty();
    }
}
