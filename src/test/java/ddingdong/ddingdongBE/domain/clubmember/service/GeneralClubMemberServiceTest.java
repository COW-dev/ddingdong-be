package ddingdong.ddingdongBE.domain.clubmember.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeneralClubMemberServiceTest extends TestContainerSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private ClubMemberService clubMemberService;

    @DisplayName("존재하는 동아리원 ID로 조회하면 동아리원을 반환한다")
    @Test
    void getByClubMemberId() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        ClubMember savedClubMember = clubMemberRepository.save(
                ClubMemberFixture.createClubMember(savedClub));

        // when
        ClubMember found = clubMemberService.getById(savedClubMember.getId());

        // then
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(savedClubMember.getId());
        assertThat(found.getName()).isEqualTo(savedClubMember.getName());
    }

    @DisplayName("존재하지 않는 동아리원 ID로 조회하면 예외를 발생시킨다.")
    @Test
    void getByClubMemberIdWhenNotExists() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        clubMemberRepository.save(ClubMemberFixture.createClubMember(savedClub));

        Long notExistId = -1L;

        // when & then
        assertThatThrownBy(() -> clubMemberService.getById(notExistId))
                .isInstanceOf(ResourceNotFound.class);
    }

    @DisplayName("동아리의 ID로 동아리원을 전체 삭제(Soft Delete)한다.")
    @Test
    void deleteAllByClubId() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser("1234"));
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        ClubMember clubMember1 = ClubMemberFixture.createClubMember(savedClub);
        ClubMember clubMember2 = ClubMemberFixture.createClubMember(savedClub);
        clubMemberRepository.saveAll(List.of(clubMember1, clubMember2));

        User otherUser = userRepository.save(UserFixture.createClubUser("5678"));
        Club otherClub = clubRepository.save(ClubFixture.createClub(otherUser));
        ClubMember otherClubMember = clubMemberRepository.save(
                ClubMemberFixture.createClubMember(otherClub));

        // when
        clubMemberService.deleteAllByClubId(savedClub.getId());

        // then
        List<ClubMember> remainingMembers = clubMemberRepository.findAll();
        assertThat(remainingMembers).hasSize(1);
        assertThat(remainingMembers)
                .extracting(ClubMember::getId)
                .containsExactly(otherClubMember.getId());
    }
}
