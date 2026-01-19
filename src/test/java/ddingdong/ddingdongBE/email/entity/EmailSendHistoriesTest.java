package ddingdong.ddingdongBE.email.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailSendHistoriesTest {

    @DisplayName("전체 이메일 개수를 반환한다")
    @Test
    void getTotalCount() {
        // given
        EmailSendHistory email1 = createEmailWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory email2 = createEmailWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory email3 = createEmailWithStatus(EmailSendStatus.PENDING);
        EmailSendHistories emailSendHistories = new EmailSendHistories(List.of(email1, email2, email3));

        // when
        int totalCount = emailSendHistories.getTotalCount();

        // then
        assertThat(totalCount).isEqualTo(3);
    }

    @DisplayName("전송 성공한 이메일 개수를 반환한다")
    @Test
    void getSuccessCount() {
        // given
        EmailSendHistory successEmail1 = createEmailWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory successEmail2 = createEmailWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory failEmail = createEmailWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory pendingEmail = createEmailWithStatus(EmailSendStatus.PENDING);
        EmailSendHistories emailSendHistories = new EmailSendHistories(
                List.of(successEmail1, successEmail2, failEmail, pendingEmail));

        // when
        int successCount = emailSendHistories.getSuccessCount();

        // then
        assertThat(successCount).isEqualTo(2);
    }

    @DisplayName("전송 실패한 이메일 개수를 반환한다")
    @Test
    void getFailCount() {
        // given
        EmailSendHistory successEmail = createEmailWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory temporaryFailEmail = createEmailWithStatus(EmailSendStatus.TEMPORARY_FAILURE);
        EmailSendHistory permanentFailEmail = createEmailWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory bounceRejectEmail = createEmailWithStatus(EmailSendStatus.BOUNCE_REJECT);
        EmailSendHistory complaintRejectEmail = createEmailWithStatus(EmailSendStatus.COMPLAINT_REJECT);
        EmailSendHistories emailSendHistories = new EmailSendHistories(
                List.of(successEmail, temporaryFailEmail, permanentFailEmail, bounceRejectEmail, complaintRejectEmail));

        // when
        int failCount = emailSendHistories.getFailCount();

        // then
        assertThat(failCount).isEqualTo(4);
    }

    @DisplayName("이메일 목록이 비어있으면 모든 카운트가 0이다")
    @Test
    void getCountsWhenEmpty() {
        // given
        EmailSendHistories emailSendHistories = new EmailSendHistories(List.of());

        // when & then
        assertThat(emailSendHistories.getTotalCount()).isEqualTo(0);
        assertThat(emailSendHistories.getSuccessCount()).isEqualTo(0);
        assertThat(emailSendHistories.getFailCount()).isEqualTo(0);
    }

    @DisplayName("PENDING, SENDING 상태는 성공도 실패도 아니다")
    @Test
    void getPendingAndSendingAreNotSuccessOrFail() {
        // given
        EmailSendHistory pendingEmail = createEmailWithStatus(EmailSendStatus.PENDING);
        EmailSendHistory sendingEmail = createEmailWithStatus(EmailSendStatus.SENDING);
        EmailSendHistories emailSendHistories = new EmailSendHistories(List.of(pendingEmail, sendingEmail));

        // when & then
        assertThat(emailSendHistories.getTotalCount()).isEqualTo(2);
        assertThat(emailSendHistories.getSuccessCount()).isEqualTo(0);
        assertThat(emailSendHistories.getFailCount()).isEqualTo(0);
    }

    private EmailSendHistory createEmailWithStatus(EmailSendStatus status) {
        return EmailSendHistory.builder()
                .status(status)
                .retryCount(0)
                .build();
    }
}