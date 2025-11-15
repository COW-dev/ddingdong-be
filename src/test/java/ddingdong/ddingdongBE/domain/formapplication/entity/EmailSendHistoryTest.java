ㄹimport static ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendStatus.PENDING;
import static ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendStatus.PERMANENT_FAILURE;
import static ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendStatus.SENDING;
import static ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendStatus.TEMPORARY_FAILURE;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.FormApplicationFixture;
import ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailSendHistoryTest {

    private EmailSendHistory emailSendHistory;
    private FormApplication formApplication;

    @BeforeEach
    void setUp() {
        formApplication = FormApplicationFixture.pendingFormApplication();
    }

    @DisplayName("PENDING 상태로 EmailSendHistory를 생성할 수 있다")
    @Test
    void createPending() {
        // when
        emailSendHistory = EmailSendHistory.createPending(formApplication);

        // then
        assertThat(emailSendHistory.getStatus()).isEqualTo(PENDING);
        assertThat(emailSendHistory.getFormApplication()).isEqualTo(formApplication);
        assertThat(emailSendHistory.getRetryCount()).isZero();
        assertThat(emailSendHistory.getSentAt()).isNull();
        assertThat(emailSendHistory.getMessageTrackingId()).isNull();
    }

    @DisplayName("PENDING 상태에서 trySend()를 호출하면 SENDING 상태로 변경되고 sentAt이 설정된다")
    @Test
    void trySend_from_pending() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);
        LocalDateTime beforeCall = LocalDateTime.now().minusSeconds(1);

        // when
        emailSendHistory.trySend();

        // then
        assertThat(emailSendHistory.getStatus()).isEqualTo(SENDING);
        assertThat(emailSendHistory.getRetryCount()).isZero();
        assertThat(emailSendHistory.getSentAt()).isAfter(beforeCall);
        assertThat(emailSendHistory.getSentAt()).isBefore(LocalDateTime.now().plusSeconds(1));
    }

    @DisplayName("SENDING 상태에서 trySend()를 호출하면 재시도 횟수가 증가한다")
    @Test
    void trySend_from_sending() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);
        emailSendHistory.trySend(); // PENDING -> SENDING
        LocalDateTime firstSentAt = emailSendHistory.getSentAt();

        // when
        emailSendHistory.trySend(); // SENDING -> SENDING (retry)

        // then
        assertThat(emailSendHistory.getStatus()).isEqualTo(SENDING);
        assertThat(emailSendHistory.getRetryCount()).isEqualTo(1);
        assertThat(emailSendHistory.getSentAt()).isAfter(firstSentAt);
    }

    @DisplayName("여러 번 재시도할 때마다 재시도 횟수가 증가한다")
    @Test
    void trySend_multiple_retries() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);
        emailSendHistory.trySend(); // PENDING -> SENDING

        // when & then
        for (int i = 1; i <= 3; i++) {
            emailSendHistory.trySend();
            assertThat(emailSendHistory.getRetryCount()).isEqualTo(i);
            assertThat(emailSendHistory.getStatus()).isEqualTo(SENDING);
        }
    }

    @DisplayName("markRetryFail()을 호출하면 TEMPORARY_FAILURE 상태로 변경된다")
    @Test
    void markRetryFail() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);

        // when
        emailSendHistory.markRetryFail();

        // then
        assertThat(emailSendHistory.getStatus()).isEqualTo(TEMPORARY_FAILURE);
    }

    @DisplayName("markNonRetryFail()을 호출하면 PERMANENT_FAILURE 상태로 변경된다")
    @Test
    void markNonRetryFail() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);

        // when
        emailSendHistory.markNonRetryFail();

        // then
        assertThat(emailSendHistory.getStatus()).isEqualTo(PERMANENT_FAILURE);
    }

    @DisplayName("updateMessageTrackingId()를 호출하면 messageTrackingId가 설정된다")
    @Test
    void updateMessageTrackingId() {
        // given
        emailSendHistory = EmailSendHistory.createPending(formApplication);
        String messageId = "test-message-id-12345";

        // when
        emailSendHistory.updateMessageTrackingId(messageId);

        // then
        assertThat(emailSendHistory.getMessageTrackingId()).isEqualTo(messageId);
    }
}
