package ddingdong.ddingdongBE.domain.form.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.EmailException.NoEmailReSendTargetException;
import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.ClubMemberFixture;
import ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
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
import ddingdong.ddingdongBE.domain.form.service.dto.command.ResendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.command.SendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendCountQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusQuery;
import ddingdong.ddingdongBE.domain.form.service.dto.query.EmailSendStatusQuery.EmailSendStatusInfoQuery;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import ddingdong.ddingdongBE.email.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

    @MockitoBean
    private ApplicationEventPublisher applicationEventPublisher;

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
                .filter(clubMember -> Objects.equals(formApplication.getName(),
                        clubMember.getName()))
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
                .filter(clubMember -> Objects.equals(formApplication.getName(),
                        clubMember.getName()))
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
        FormEmailSendHistory savedFormEmailSendHistory = formEmailSendHistoryRepository.save(
                formEmailSendHistory);

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
        FormEmailSendHistory savedFormEmailSendHistory = formEmailSendHistoryRepository.save(
                formEmailSendHistory);

        // when
        EmailSendCountQuery result = facadeCentralFormService.getEmailSendCountByFormEmailSendHistoryId(
                savedFormEmailSendHistory.getId());

        // then
        assertThat(result.totalCount()).isEqualTo(0);
        assertThat(result.successCount()).isEqualTo(0);
        assertThat(result.failCount()).isEqualTo(0);
    }

    @DisplayName("이메일 전송 시 FormEmailSendHistory가 생성된다")
    @Test
    void sendApplicationResultEmailCreatesFormEmailSendHistory() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormApplication formApplication1 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        FormApplication formApplication2 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        formApplicationRepository.save(formApplication1);
        formApplicationRepository.save(formApplication2);

        SendApplicationResultEmailCommand command = new SendApplicationResultEmailCommand(
                savedUser.getId(),
                savedForm.getId(),
                "1차 합격 안내",
                FormApplicationStatus.FIRST_PASS,
                "축하합니다. 1차 합격하셨습니다."
        );

        // when
        facadeCentralFormService.sendApplicationResultEmail(command);

        // then
        List<FormEmailSendHistory> formEmailSendHistories = formEmailSendHistoryRepository.findAll();
        assertThat(formEmailSendHistories).hasSize(1);

        FormEmailSendHistory savedHistory = formEmailSendHistories.get(0);
        assertThat(savedHistory.getFormApplicationStatus()).isEqualTo(
                FormApplicationStatus.FIRST_PASS);
        assertThat(savedHistory.getEmailContent()).isEqualTo("축하합니다. 1차 합격하셨습니다.");
        assertThat(savedHistory.getForm().getId()).isEqualTo(savedForm.getId());
    }

    @DisplayName("이메일 전송 시 각 지원자에 대해 EmailSendHistory가 생성된다")
    @Test
    void sendApplicationResultEmailCreatesEmailSendHistoryForEachApplication() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormApplication formApplication1 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        FormApplication formApplication2 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        FormApplication formApplication3 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.SUBMITTED);
        formApplicationRepository.save(formApplication1);
        formApplicationRepository.save(formApplication2);
        formApplicationRepository.save(formApplication3);

        SendApplicationResultEmailCommand command = new SendApplicationResultEmailCommand(
                savedUser.getId(),
                savedForm.getId(),
                "1차 합격 안내",
                FormApplicationStatus.FIRST_PASS,
                "축하합니다. 1차 합격하셨습니다."
        );

        // when
        facadeCentralFormService.sendApplicationResultEmail(command);

        // then
        List<EmailSendHistory> emailSendHistories = emailSendHistoryRepository.findAll();
        assertThat(emailSendHistories).hasSize(2);

        FormEmailSendHistory formEmailSendHistory = formEmailSendHistoryRepository.findAll().get(0);
        assertThat(emailSendHistories)
                .allMatch(history -> history.getFormEmailSendHistory().getId()
                        .equals(formEmailSendHistory.getId()));
    }


    @DisplayName("재전송 시 기존 전송 이력의 제목/본문을 재사용해 새 FormEmailSendHistory가 생성되고, 재전송 대상에 대한 EmailSendHistory가 생성된다.")
    @Test
    void resendApplicationResultEmail_CreatesNewHistoriesBasedOnLatestHistoryAndCreatesPendingEmailSendHistory() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory oldHistory = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(savedForm)
        );

        FormApplication formApplication1 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS)
        );
        FormApplication formApplication2 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS)
        );
        FormApplication formApplication3 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS)
        );

        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.temporaryFailureWithFormEmailSendHistory(formApplication1,
                        oldHistory)
        );
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(formApplication1,
                        oldHistory)
        );

        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.temporaryFailureWithFormEmailSendHistory(formApplication2,
                        oldHistory)
        );

        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(formApplication3,
                        oldHistory)
        );

        ResendApplicationResultEmailCommand command = new ResendApplicationResultEmailCommand(
                savedUser.getId(),
                savedForm.getId(),
                FormApplicationStatus.FIRST_PASS
        );

        // when
        facadeCentralFormService.resendApplicationResultEmail(command);

        // then
        List<FormEmailSendHistory> histories = formEmailSendHistoryRepository.findAll();
        assertThat(histories).hasSize(2);

        FormEmailSendHistory newHistory = histories.stream()
                .filter(h -> !h.getId().equals(oldHistory.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(newHistory.getForm().getId()).isEqualTo(savedForm.getId());
        assertThat(newHistory.getFormApplicationStatus()).isEqualTo(
                FormApplicationStatus.FIRST_PASS);
        assertThat(newHistory.getTitle()).isEqualTo(oldHistory.getTitle());
        assertThat(newHistory.getEmailContent()).isEqualTo(oldHistory.getEmailContent());

        List<EmailSendHistory> newBatchEmails =
                emailSendHistoryRepository.findAllByFormEmailSendHistoryId(newHistory.getId());

        assertThat(newBatchEmails).hasSize(1);

        EmailSendHistory pending = newBatchEmails.get(0);
        assertThat(pending.getStatus()).isEqualTo(EmailSendStatus.PENDING);
        assertThat(pending.getSentAt()).isNull();
        assertThat(pending.getFormApplication().getId()).isEqualTo(formApplication2.getId());
    }

    @DisplayName("재전송 대상이 없으면 NoEmailReSendTargetException이 발생하고 FormEmailSendHistory가 추가로 생성되지 않는다")
    @Test
    void resendApplicationResultEmailThrowsExceptionWhenNoTargets() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory oldHistory = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(savedForm)
        );

        FormApplication formApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS)
        );

        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(formApplication,
                        oldHistory)
        );

        ResendApplicationResultEmailCommand command = new ResendApplicationResultEmailCommand(
                savedUser.getId(),
                savedForm.getId(),
                FormApplicationStatus.FIRST_PASS
        );

        // when & then
        assertThatThrownBy(() -> facadeCentralFormService.resendApplicationResultEmail(command))
                .isInstanceOf(NoEmailReSendTargetException.class)
                .hasMessage("재전송할 이메일 대상이 없습니다.");

        assertThat(formEmailSendHistoryRepository.findAll()).hasSize(1);
    }

    @DisplayName("폼 ID와 지원 결과 상태로 이메일 전송 현황을 조회할 수 있다")
    @Test
    void getEmailSendStatusByFormIdAndFormApplicationStatus() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormEmailSendHistory formEmailSendHistory = FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(
                savedForm);
        FormEmailSendHistory savedFormEmailSendHistory = formEmailSendHistoryRepository.save(
                formEmailSendHistory);

        FormApplication formApplication1 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        FormApplication formApplication2 = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FIRST_PASS);
        formApplicationRepository.save(formApplication1);
        formApplicationRepository.save(formApplication2);

        EmailSendHistory successEmail = EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(
                formApplication1, savedFormEmailSendHistory);
        EmailSendHistory failEmail = EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(
                formApplication2, savedFormEmailSendHistory);
        emailSendHistoryRepository.save(successEmail);
        emailSendHistoryRepository.save(failEmail);

        // when
        EmailSendStatusQuery result = facadeCentralFormService.getEmailSendStatusByFormIdAndFormApplicationStatus(
                savedForm.getId(), FormApplicationStatus.FIRST_PASS.name());

        // then
        assertThat(result.emailSendStatusInfoQueries()).hasSize(2);
    }

    @DisplayName("같은 지원자에게 여러 번 이메일을 전송한 경우, 같은 상태 내에서 가장 최신 전송 기록만 조회된다")
    @Test
    void getEmailSendStatusByFormIdAndFormApplicationStatusReturnsLatestOnly() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        FormEmailSendHistory finalPassHistory = FormEmailSendHistoryFixture.createFormEmailSendHistoryForFinalPass(
                savedForm);
        FormEmailSendHistory savedFinalPassHistory = formEmailSendHistoryRepository.save(
                finalPassHistory);

        FormApplication formApplication = FormApplicationFixture.create(savedForm,
                FormApplicationStatus.FINAL_PASS);
        formApplicationRepository.save(formApplication);

        // 같은 지원자에게 최종 합격 이메일을 2번 전송
        EmailSendHistory olderFinalPassEmail = EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(
                formApplication, savedFinalPassHistory);
        EmailSendHistory latestFinalPassEmail = EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(
                formApplication, savedFinalPassHistory);
        emailSendHistoryRepository.save(olderFinalPassEmail);
        emailSendHistoryRepository.save(latestFinalPassEmail);

        // when
        EmailSendStatusQuery result = facadeCentralFormService.getEmailSendStatusByFormIdAndFormApplicationStatus(
                savedForm.getId(), FormApplicationStatus.FINAL_PASS.name());

        // then
        assertThat(result.emailSendStatusInfoQueries()).hasSize(1);
        EmailSendStatusInfoQuery statusInfo = result.emailSendStatusInfoQueries().get(0);
        assertThat(statusInfo.formApplicationStatus()).isEqualTo(FormApplicationStatus.FINAL_PASS);
    }

    @DisplayName("이메일 전송 기록이 없으면 빈 목록을 반환한다")
    @Test
    void getEmailSendStatusByFormIdWithNoEmails() {
        // given
        User user = UserFixture.createClubUser();
        User savedUser = userRepository.save(user);
        Club club = ClubFixture.createClub(savedUser);
        Club savedClub = clubRepository.save(club);

        Form form = FormFixture.createForm(savedClub);
        Form savedForm = formRepository.save(form);

        // when
        EmailSendStatusQuery result = facadeCentralFormService.getEmailSendStatusByFormIdAndFormApplicationStatus(
                savedForm.getId(), FormApplicationStatus.FIRST_PASS.name());

        // then
        assertThat(result.emailSendStatusInfoQueries()).isEmpty();
    }
}

