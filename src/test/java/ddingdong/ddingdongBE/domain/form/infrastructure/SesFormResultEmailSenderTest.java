package ddingdong.ddingdongBE.domain.form.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.fixture.FormEmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.entity.FormResultSendingEmailInfo;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.form.service.FormResultEmailSender;
import ddingdong.ddingdongBE.email.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.email.entity.EmailContent;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SesFormResultEmailSenderTest extends TestContainerSupport {

    @Autowired
    private FormResultEmailSender formResultEmailSender;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    @Autowired
    private FormEmailSendHistoryRepository formEmailSendHistoryRepository;

    private FormApplication formApplication;
    private EmailContent emailContent;
    private EmailSendHistory emailSendHistory;
    private FormEmailSendHistory formEmailSendHistory;

    @BeforeEach
    void setUp() {
        formApplication = FormApplicationFixture.pendingFormApplication();
        FormApplication savedFormApplication = formApplicationRepository.save(formApplication);
        emailContent = EmailContent.of("테스트 제목", "테스트 내용입니다. 안녕하세요 {지원자명}님", ClubFixture.createClub());

        formEmailSendHistory = FormEmailSendHistoryFixture.createFormEmailSendHistoryForFirstPass(null);
        formEmailSendHistory = formEmailSendHistoryRepository.save(formEmailSendHistory);

        // 실제 EmailSendHistory 엔티티 생성
        emailSendHistory = EmailSendHistory.createPending(savedFormApplication, formEmailSendHistory);
        emailSendHistory = emailSendHistoryRepository.save(emailSendHistory);
    }

    @DisplayName("이메일을 성공적으로 전송한다")
    @Test
    void sendResult_success() {
        // when
        FormResultSendingEmailInfo formResultSendingEmailInfo = new FormResultSendingEmailInfo(emailSendHistory.getId(),
                formApplication.getEmail(), formApplication.getName(),
                emailContent);
        formResultEmailSender.sendResult(formResultSendingEmailInfo);

        // then
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getMessageTrackingId()).isNotNull();
        assertThat(updatedHistory.getSentAt()).isNotNull();
    }
}
