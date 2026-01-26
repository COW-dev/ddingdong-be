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
                FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(savedForm)
        );

        // FINAL_PASS 지원서 - applicationStatus 필터링 검증용
        FormEmailSendHistory finalPassHistory = formEmailSendHistoryRepository.save(
                FormEmailSendHistoryFixture.createFormEmailSendHistoryForFinalPass(savedForm)
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
                EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(formApplication1, firstPassHistory)
        );

        // formApplication2: 재전송 시 TEMPORARY_FAILURE -> 결과에 포함
        emailSendHistoryRepository.save(
                EmailSendHistoryFixture.deliverySuccessWithFormEmailSendHistory(formApplication2,
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
}
