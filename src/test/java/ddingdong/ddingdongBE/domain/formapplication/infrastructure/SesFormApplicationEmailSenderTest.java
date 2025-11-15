package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.ClubFixture;
import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.formapplication.entity.EmailContent;
import ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.email.EmailSendHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SesFormApplicationEmailSenderTest extends TestContainerSupport {

    @Autowired
    private SesFormApplicationEmailSender sesFormApplicationEmailSender;

    @Autowired
    private EmailSendHistoryService emailSendHistoryService;

    @Autowired
    private EmailSendHistoryRepository emailSendHistoryRepository;

    @Autowired
    private SesClientPort sesClientPort;

    private FormApplication formApplication;
    private EmailContent emailContent;
    private EmailSendHistory emailSendHistory;

    @BeforeEach
    void setUp() {
        formApplication = FormApplicationFixture.pendingFormApplication();
        emailContent = EmailContent.of("테스트 제목", "테스트 내용입니다. 안녕하세요 {지원자명}님", ClubFixture.createClub());
        
        // 실제 EmailSendHistory 엔티티 생성
        emailSendHistory = EmailSendHistory.createPending(formApplication);
        emailSendHistory = emailSendHistoryRepository.save(emailSendHistory);
    }

    @DisplayName("이메일을 성공적으로 전송한다")
    @Test
    void sendResult_success() {
        // when
        sesFormApplicationEmailSender.sendResult(formApplication, emailContent, emailSendHistory.getId());

        // then
        EmailSendHistory updatedHistory = emailSendHistoryRepository.findById(emailSendHistory.getId()).orElseThrow();
        assertThat(updatedHistory.getMessageTrackingId()).isNotNull();
        assertThat(updatedHistory.getSentAt()).isNotNull();
    }
}
