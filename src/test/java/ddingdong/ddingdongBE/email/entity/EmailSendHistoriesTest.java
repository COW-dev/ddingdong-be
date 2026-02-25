package ddingdong.ddingdongBE.email.entity;

import static ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture.createWithIdAndSentAt;
import static ddingdong.ddingdongBE.common.fixture.EmailSendHistoryFixture.createWithStatus;
import static ddingdong.ddingdongBE.common.fixture.FormApplicationFixture.createWithId;
import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailSendHistoriesTest {

    @DisplayName("전체 이메일 개수를 반환한다")
    @Test
    void getTotalCount() {
        // given
        EmailSendHistory email1 = createWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory email2 = createWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory email3 = createWithStatus(EmailSendStatus.PENDING);
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
        EmailSendHistory successEmail1 = createWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory successEmail2 = createWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory failEmail = createWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory pendingEmail = createWithStatus(EmailSendStatus.PENDING);
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
        EmailSendHistory successEmail = createWithStatus(EmailSendStatus.DELIVERY_SUCCESS);
        EmailSendHistory temporaryFailEmail = createWithStatus(EmailSendStatus.TEMPORARY_FAILURE);
        EmailSendHistory permanentFailEmail = createWithStatus(EmailSendStatus.PERMANENT_FAILURE);
        EmailSendHistory bounceRejectEmail = createWithStatus(EmailSendStatus.BOUNCE_REJECT);
        EmailSendHistory complaintRejectEmail = createWithStatus(EmailSendStatus.COMPLAINT_REJECT);
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
        EmailSendHistory pendingEmail = createWithStatus(EmailSendStatus.PENDING);
        EmailSendHistory sendingEmail = createWithStatus(EmailSendStatus.SENDING);
        EmailSendHistories emailSendHistories = new EmailSendHistories(List.of(pendingEmail, sendingEmail));

        // when & then
        assertThat(emailSendHistories.getTotalCount()).isEqualTo(2);
        assertThat(emailSendHistories.getSuccessCount()).isEqualTo(0);
        assertThat(emailSendHistories.getFailCount()).isEqualTo(0);
    }

    @DisplayName("FormApplication별로 가장 최신 EmailSendHistory만 반환한다")
    @Test
    void getLatestByFormApplication() {
        // given
        FormApplication application1 = createWithId(1L);
        FormApplication application2 = createWithId(2L);

        EmailSendHistory oldEmail1 = createWithIdAndSentAt(
                application1, EmailSendStatus.PENDING, LocalDateTime.of(2024, 1, 1, 10, 0), 1L);
        EmailSendHistory newEmail1 = createWithIdAndSentAt(
                application1, EmailSendStatus.DELIVERY_SUCCESS, LocalDateTime.of(2024, 1, 2, 10, 0), 2L);
        EmailSendHistory email2 = createWithIdAndSentAt(
                application2, EmailSendStatus.PERMANENT_FAILURE, LocalDateTime.of(2024, 1, 1, 10, 0), 3L);

        EmailSendHistories emailSendHistories = new EmailSendHistories(
                List.of(oldEmail1, newEmail1, email2));

        // when
        EmailSendHistories latestHistories = emailSendHistories.getLatestByFormApplication();

        // then
        assertThat(latestHistories.getTotalCount()).isEqualTo(2);
        assertThat(latestHistories.getSuccessCount()).isEqualTo(1);
        assertThat(latestHistories.getFailCount()).isEqualTo(1);
    }

    @DisplayName("sentAt이 같으면 id가 큰 것을 최신으로 판단한다")
    @Test
    void getLatestByFormApplicationWithSameSentAt() {
        // given
        FormApplication application = createWithId(1L);
        LocalDateTime sameSentAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        EmailSendHistory smallerIdEmail = createWithIdAndSentAt(
                application, EmailSendStatus.PENDING, sameSentAt, 1L);
        EmailSendHistory largerIdEmail = createWithIdAndSentAt(
                application, EmailSendStatus.DELIVERY_SUCCESS, sameSentAt, 2L);

        EmailSendHistories emailSendHistories = new EmailSendHistories(
                List.of(smallerIdEmail, largerIdEmail));

        // when
        EmailSendHistories latestHistories = emailSendHistories.getLatestByFormApplication();

        // then
        assertThat(latestHistories.getTotalCount()).isEqualTo(1);
        assertThat(latestHistories.getSuccessCount()).isEqualTo(1);
    }

    @DisplayName("sentAt이 null인 경우 null이 아닌 것을 최신으로 판단한다")
    @Test
    void getLatestByFormApplicationWithNullSentAt() {
        // given
        FormApplication application = createWithId(1L);

        EmailSendHistory nullSentAtEmail = createWithIdAndSentAt(
                application, EmailSendStatus.PENDING, null, 1L);
        EmailSendHistory nonNullSentAtEmail = createWithIdAndSentAt(
                application, EmailSendStatus.DELIVERY_SUCCESS, LocalDateTime.of(2024, 1, 1, 10, 0), 2L);

        EmailSendHistories emailSendHistories = new EmailSendHistories(
                List.of(nullSentAtEmail, nonNullSentAtEmail));

        // when
        EmailSendHistories latestHistories = emailSendHistories.getLatestByFormApplication();

        // then
        assertThat(latestHistories.getTotalCount()).isEqualTo(1);
        assertThat(latestHistories.getSuccessCount()).isEqualTo(1);
    }

    @DisplayName("빈 목록에서 getLatestByFormApplication을 호출하면 빈 결과를 반환한다")
    @Test
    void getLatestByFormApplicationWithEmptyList() {
        // given
        EmailSendHistories emailSendHistories = new EmailSendHistories(List.of());

        // when
        EmailSendHistories latestHistories = emailSendHistories.getLatestByFormApplication();

        // then
        assertThat(latestHistories.getTotalCount()).isEqualTo(0);
    }
}
