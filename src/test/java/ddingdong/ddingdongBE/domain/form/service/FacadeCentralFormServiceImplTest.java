package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormEmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.clubmember.entity.ClubMember;
import ddingdong.ddingdongBE.domain.clubmember.repository.ClubMemberRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendCountQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
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
    private FormEmailSendHistoryRepository formEmailSendHistoryRepository;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

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

    @DisplayName("폼 이메일 전송 기록(FormEmailSendHistory) ID로 이메일 전송 현황 카운트를 조회할 수 있다")
    @Test
    void getEmailSendCountByFormEmailSendHistoryId() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormEmailSendHistory formEmailSendHistory = FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(
                savedForm);
        FormEmailSendHistory savedFormEmailSendHistory = formEmailSendHistoryRepository.save(formEmailSendHistory);

        FormApplication formApplication1 = FormFixture.createFormApplicationFinalPass(savedForm);
        FormApplication formApplication2 = FormFixture.createFormApplicationFinalPass(savedForm);
        FormApplication formApplication3 = FormFixture.createFormApplicationFinalPass(savedForm);
        formApplicationRepository.save(formApplication1);
        formApplicationRepository.save(formApplication2);
        formApplicationRepository.save(formApplication3);

        EmailSendHistory successEmail1 = EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(
                formApplication1, savedFormEmailSendHistory);
        EmailSendHistory successEmail2 = EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(
                formApplication2, savedFormEmailSendHistory);
        EmailSendHistory failEmail = EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(
                formApplication3, savedFormEmailSendHistory);
        emailSendHistoryRepository.save(successEmail1);
        emailSendHistoryRepository.save(successEmail2);
        emailSendHistoryRepository.save(failEmail);

        // when
        EmailSendCountQuery result = facadeCentralFormService.getEmailSendCountByFormEmailSendHistoryId(
                savedFormEmailSendHistory.getId());

        // then
        assertThat(result.totalCount()).isEqualTo(3);
        assertThat(result.successCount()).isEqualTo(2);
        assertThat(result.failCount()).isEqualTo(1);
    }

    @DisplayName("이메일 전송 기록이 없으면 모든 카운트가 0이다")
    @Test
    void getEmailSendCountByFormEmailSendHistoryIdWithNoEmails() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormEmailSendHistory formEmailSendHistory = FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(
                savedForm);
        FormEmailSendHistory savedFormEmailSendHistory = formEmailSendHistoryRepository.save(formEmailSendHistory);

        // when
        EmailSendCountQuery result = facadeCentralFormService.getEmailSendCountByFormEmailSendHistoryId(
                savedFormEmailSendHistory.getId());

        // then
        assertThat(result.totalCount()).isEqualTo(0);
        assertThat(result.successCount()).isEqualTo(0);
        assertThat(result.failCount()).isEqualTo(0);
    }
}
