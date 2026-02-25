package ddingdong.ddingdongBE.domain.club.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.club.service.dto.command.UpdateClubInfoCommand;
import ddingdong.ddingdongBE.domain.club.service.dto.query.MyClubInfoQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeCentralClubServiceImplTest extends TestContainerSupport {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FacadeCentralClubService facadeCentralClubService;

    @DisplayName("중앙동아리: 내 동아리 정보 조회")
    @Test
    void getMyClubInfo() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));

        // when
        MyClubInfoQuery result = facadeCentralClubService.getMyClubInfo(savedUser.getId());

        // then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(savedClub.getName());
        assertThat(result.category()).isEqualTo(savedClub.getCategory());
        assertThat(result.tag()).isEqualTo(savedClub.getTag());
        assertThat(result.leader()).isEqualTo(savedClub.getLeader());
        assertThat(result.phoneNumber()).isEqualTo(savedClub.getPhoneNumber().getNumber());
        assertThat(result.location()).isEqualTo(savedClub.getLocation().getValue());
    }

    @DisplayName("중앙동아리: 동아리 정보 수정")
    @Test
    void updateClubInfo() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));

        UpdateClubInfoCommand command = new UpdateClubInfoCommand(
                savedUser.getId(),
                "updatedName",
                "updatedCategory",
                "updatedTag",
                "updatedClubLeader",
                "010-1234-5678",
                "S1111",
                "updatedRegularMeeting",
                "updatedIntroduction",
                "updatedActivity",
                "updatedIdeal",
                null,
                null
        );

        // when
        facadeCentralClubService.updateClubInfo(command);

        // then
        Optional<Club> result = clubRepository.findById(savedClub.getId());
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo(command.name());
        assertThat(result.get().getCategory()).isEqualTo(command.category());
        assertThat(result.get().getLeader()).isEqualTo(command.clubLeader());
    }
}
