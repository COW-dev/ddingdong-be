package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class FacadeCentralFormServiceImplTest extends TestContainerSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ClubMemberRepository clubMemberRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private FacadeCentralFormService facadeCentralFormService;

    @DisplayName("지원자를 동아리원 명단에 등록할 수 있다")
    @Test
    void registerFormApplication() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);
        FormApplication formApplication = FormFixture.createFormApplicationFinalPass(savedForm);
        formApplicationRepository.save(formApplication);

        // when
        facadeCentralFormService.registerApplicantAsMember(savedUser.getId(), savedForm.getId());

        // then
        Club found = clubRepository.findById(savedClub.getId()).orElse(null);
        Optional<ClubMember> registerClubMember = found.getClubMembers().stream()
                .filter(clubMember -> Objects.equals(formApplication.getName(), clubMember.getName()))
                .findFirst();
        assertThat(registerClubMember).isNotEmpty();
    }

    @DisplayName("지원자를 동아리원 명단에 등록할 때, 기존 인원은 모두 삭제된다.")
    @Test
    void registerFormApplicationDelete() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        ClubMember origin = ClubMemberFixture.createClubMember(club);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);
        FormApplication formApplication = FormFixture.createFormApplicationFinalPass(savedForm);
        formApplicationRepository.save(formApplication);

        // when
        facadeCentralFormService.registerApplicantAsMember(savedUser.getId(), savedForm.getId());

        // then
        Club found = clubRepository.findById(savedClub.getId()).orElse(null);
        Optional<ClubMember> registerClubMember = found.getClubMembers().stream()
                .filter(clubMember -> Objects.equals(formApplication.getName(), clubMember.getName()))
                .findFirst();
        Optional<ClubMember> originClubMember = found.getClubMembers().stream()
                .filter(clubMember -> Objects.equals(origin.getName(), clubMember.getName()))
                .findFirst();
        assertThat(registerClubMember).isNotEmpty();
        assertThat(originClubMember).isEmpty();
    }
}
