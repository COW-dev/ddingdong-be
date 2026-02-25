package ddingdong.ddingdongBE.email.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.email.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.repository.FormApplicationRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmailSendHistoryServiceIntegrationTest extends TestContainerSupport {

    @Autowired
    private EmailSendHistoryService emailSendHistoryService;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @Autowired
    private FormApplicationRepository formApplicationRepository;

    private FormApplication formApplication;
    private EmailSendHistory emailSendHistory;
    private String messageTrackingId;

    @BeforeEach
    void setUp() {
        formApplication = FormApplicationFixture.pendingFormApplication();
        formApplication = formApplicationRepository.save(formApplication);
        
        messageTrackingId = "msg-integration-test-12345";
        emailSendHistory = EmailSendHistoryFixture.createWithMessageId(formApplication, messageTrackingId);
        emailSendHistory = emailSendHistoryRepository.save(emailSendHistory);
    }

    @DisplayName("이메일 전송 상태를 Delivery로 성공적으로 업데이트한다")
    @Test
    void updateEmailSendStatus_delivery_success() {
        // when
        emailSendHistoryService.updateEmailSendStatus("Delivery", messageTrackingId);

        // then
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getStatus()).isEqualTo(EmailSendStatus.DELIVERY_SUCCESS);
    }

    @DisplayName("이메일 전송 상태를 Bounce로 성공적으로 업데이트한다")
    @Test
    void updateEmailSendStatus_bounce_success() {
        // when
        emailSendHistoryService.updateEmailSendStatus("Bounce", messageTrackingId);

        // then
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getStatus()).isEqualTo(EmailSendStatus.BOUNCE_REJECT);
    }

    @DisplayName("이메일 전송 상태를 Complaint로 성공적으로 업데이트한다")
    @Test
    void updateEmailSendStatus_complaint_success() {
        // when
        emailSendHistoryService.updateEmailSendStatus("Complaint", messageTrackingId);

        // then
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getStatus()).isEqualTo(EmailSendStatus.COMPLAINT_REJECT);
    }

    @DisplayName("존재하지 않는 messageTrackingId로 상태 업데이트 시 예외를 발생시킨다")
    @Test
    void updateEmailSendStatus_notFound_messageId() {
        // given
        String nonExistentMessageId = "non-existent-message-id";

        // when & then
        assertThatThrownBy(() -> 
                emailSendHistoryService.updateEmailSendStatus("Delivery", nonExistentMessageId)
        ).isInstanceOf(ResourceNotFound.class);
    }

    @DisplayName("잘못된 eventType으로 상태 업데이트 시 예외를 발생시킨다")
    @Test
    void updateEmailSendStatus_invalid_eventType() {
        // given
        String invalidEventType = "InvalidEventType";

        // when & then
        assertThatThrownBy(() -> 
                emailSendHistoryService.updateEmailSendStatus(invalidEventType, messageTrackingId)
        ).isInstanceOf(ResourceNotFound.class);
    }
}
