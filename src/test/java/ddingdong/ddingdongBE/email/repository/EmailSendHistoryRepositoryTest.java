package ddingdong.ddingdongBE.email.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.fixture.FormEmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.club.repository.ClubRepository;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.form.repository.FormRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class EmailSendHistoryRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private FormRepository formRepository;
    @Autowired
    private FormApplicationRepository formApplicationRepository;
    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;
    @Autowired
    private FormEmailSendHistoryRepository formEmailSendHistoryRepository;


    @DisplayName("지원자별 최신 전송 이력을 상태/합불 조건으로 필터링해 조회한다.")
    @Test
    void findLatestByFormIdAndStatusesAndApplicationStatus() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        // FIRST_PASS 지원서
        FormEmailSendHistory firstPassHistory = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(savedForm)
        );

        // FINAL_PASS 지원서 - applicationStatus 필터링 검증용
        FormEmailSendHistory finalPassHistory = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFinalPass(savedForm)
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

        // formApplication1: 성공적으로 전송된 이메일 History -> 결과에서 제외
        EmailSendHistory formApplication1Latest = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(formApplication1, firstPassHistory)
        );

        // formApplication2: 재전송 시 TEMPORARY_FAILURE -> 결과에 포함
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(formApplication2,
                        firstPassHistory)
        );
        EmailSendHistory formApplication2Latest = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.temporaryFailureWithFormEmailSendHistory(formApplication2,
                        firstPassHistory)
        );

        // formApplication3: FIRST_PASS로만 보내야 하므로 FINAL_PASS인 기록은 applicationStatus 필터로 제외
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.temporaryFailureWithFormEmailSendHistory(formApplication3,
                        firstPassHistory)
        );
        EmailSendHistory formApplication3Latest = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.temporaryFailureWithFormEmailSendHistory(formApplication3,
                        finalPassHistory)
        );

        List<EmailSendStatus> statuses = EmailSendStatus.resendTargets();


        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestByFormIdAndStatusesAndApplicationStatus(
                        savedForm.getId(),
                        statuses,
                        FormApplicationStatus.FIRST_PASS
                );

        // then
        assertThat(result).hasSize(1);

        EmailSendHistory latestResendTargetEmailSendHistory = result.get(0);

        assertAll(
                () -> assertThat(latestResendTargetEmailSendHistory.getId())
                        .isEqualTo(formApplication2Latest.getId()),
                () -> assertThat(latestResendTargetEmailSendHistory.getStatus())
                        .isEqualTo(EmailSendStatus.TEMPORARY_FAILURE),
                () -> assertThat(latestResendTargetEmailSendHistory.getFormEmailSendHistory()
                        .getFormApplicationStatus())
                        .isEqualTo(FormApplicationStatus.FIRST_PASS),
                () -> assertThat(latestResendTargetEmailSendHistory.getFormApplication().getId())
                        .isEqualTo(formApplication2.getId()),
                // 결과에서 제외됨
                () -> assertThat(latestResendTargetEmailSendHistory.getId())
                        .isNotEqualTo(formApplication1Latest.getId()),
                () -> assertThat(latestResendTargetEmailSendHistory.getId())
                        .isNotEqualTo(formApplication3Latest.getId())
        );
    }

    @DisplayName("여러 배치에 걸쳐 지원자별 가장 최신 전송 이력 1건씩 반환한다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_returnsOnePerApplication() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory batch1 = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(savedForm));
        FormEmailSendHistory batch2 = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(savedForm));

        FormApplication formApplication1 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS));
        FormApplication formApplication2 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS));

        // formApplication1: batch1 실패 → batch2 성공. 최신(batch2)이 반환돼야 한다
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(formApplication1, batch1));
        EmailSendHistory formApplication1LatestSuccess = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(formApplication1, batch2));

        // formApplication2: batch1 성공. 재전송 대상 아님
        EmailSendHistory formApplication2BatchOneSuccess = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(formApplication2, batch1));

        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        savedForm.getId(),
                        List.of(FormApplicationStatus.FIRST_PASS)
                );

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(EmailSendHistory::getId)
                .containsExactlyInAnyOrder(
                        formApplication1LatestSuccess.getId(),
                        formApplication2BatchOneSuccess.getId()
                );
    }

    @DisplayName("이전 배치에서 성공한 지원자는 재전송 배치와 무관하게 최신 이력이 반환된다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_includesApplicantsFromAllBatches() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory initialBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFinalPass(savedForm));
        FormEmailSendHistory resendBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFinalPass(savedForm));

        // 초기 발송 성공한 지원자 3명
        FormApplication successApplication1 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));
        FormApplication successApplication2 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));
        FormApplication successApplication3 = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));
        // 실패 후 재전송으로 성공한 지원자 1명
        FormApplication resendSuccessApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));

        EmailSendHistory success1 = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(successApplication1, initialBatch));
        EmailSendHistory success2 = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(successApplication2, initialBatch));
        EmailSendHistory success3 = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(successApplication3, initialBatch));

        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(resendSuccessApplication, initialBatch));
        EmailSendHistory resendSuccess = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(resendSuccessApplication, resendBatch));

        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        savedForm.getId(),
                        List.of(FormApplicationStatus.FINAL_PASS)
                );

        // then: 재전송 배치(resendBatch)만이 아닌 4명 전원의 최신 이력이 반환된다
        assertThat(result).hasSize(4);
        assertThat(result).extracting(EmailSendHistory::getId)
                .containsExactlyInAnyOrder(
                        success1.getId(),
                        success2.getId(),
                        success3.getId(),
                        resendSuccess.getId()
                );
    }

    @DisplayName("조회 대상 status에 포함되지 않은 status의 이력은 반환하지 않는다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_excludesUnrequestedStatuses() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory firstPassBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(savedForm));
        FormEmailSendHistory finalPassBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFinalPass(savedForm));

        FormApplication firstPassApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS));
        FormApplication finalPassApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));

        EmailSendHistory firstPassHistory = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(firstPassApplication, firstPassBatch));
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(finalPassApplication, finalPassBatch));

        // when: FIRST_PASS만 조회
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        savedForm.getId(),
                        List.of(FormApplicationStatus.FIRST_PASS)
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(firstPassHistory.getId());
    }

    @DisplayName("다른 폼의 이력은 조회되지 않는다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_excludesOtherFormHistories() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form targetForm = formRepository.save(FormFixture.createForm(savedClub));
        Form otherForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory targetBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(targetForm));
        FormEmailSendHistory otherBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(otherForm));

        FormApplication targetApplication = formApplicationRepository.save(
                FormApplicationFixture.create(targetForm, FormApplicationStatus.FIRST_PASS));
        FormApplication otherApplication = formApplicationRepository.save(
                FormApplicationFixture.create(otherForm, FormApplicationStatus.FIRST_PASS));

        EmailSendHistory targetHistory = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(targetApplication, targetBatch));
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(otherApplication, otherBatch));

        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        targetForm.getId(),
                        List.of(FormApplicationStatus.FIRST_PASS)
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(targetHistory.getId());
    }

    @DisplayName("전송 이력이 없으면 빈 리스트를 반환한다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_returnsEmptyWhenNoHistories() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        savedForm.getId(),
                        FormApplicationStatus.APPLICATION_RESULT_STATUSES
                );

        // then
        assertThat(result).isEmpty();
    }

    @DisplayName("여러 status를 동시에 조회할 때 각 status의 최신 이력이 모두 반환된다")
    @Test
    void findLatestPerApplicationByFormIdAndApplicationStatuses_returnsAllRequestedStatuses() {
        // given
        User savedUser = userRepository.save(UserFixture.createClubUser());
        Club savedClub = clubRepository.save(ClubFixture.createClub(savedUser));
        Form savedForm = formRepository.save(FormFixture.createForm(savedClub));

        FormEmailSendHistory firstPassBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFirstPass(savedForm));
        FormEmailSendHistory finalPassBatch = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFinalPass(savedForm));

        FormApplication firstPassApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FIRST_PASS));
        FormApplication finalPassApplication = formApplicationRepository.save(
                FormApplicationFixture.create(savedForm, FormApplicationStatus.FINAL_PASS));

        EmailSendHistory firstPassHistory = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccess(firstPassApplication, firstPassBatch));
        EmailSendHistory finalPassHistory = emailSendHistoryRepository.save(
                EmailSendHistoryFixture.permanentFailureWithFormEmailSendHistory(finalPassApplication, finalPassBatch));

        // when
        List<EmailSendHistory> result =
                emailSendHistoryRepository.findLatestPerApplicationByFormIdAndApplicationStatuses(
                        savedForm.getId(),
                        List.of(FormApplicationStatus.FIRST_PASS, FormApplicationStatus.FINAL_PASS)
                );

        // then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(EmailSendHistory::getId)
                .containsExactlyInAnyOrder(firstPassHistory.getId(), finalPassHistory.getId());
    }
}
