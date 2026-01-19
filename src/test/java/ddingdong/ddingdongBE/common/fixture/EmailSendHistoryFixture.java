package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class EmailSendHistoryFixture {

    public static EmailSendHistory pendingEmailSendHistory(FormApplication formApplication) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.PENDING)
                .retryCount(0)
                .sentAt(null)
                .build();
    }

    public static EmailSendHistory sendingEmailSendHistory(FormApplication formApplication) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.SENDING)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public static EmailSendHistory sendingEmailSendHistoryWithMessageId(FormApplication formApplication) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.SENDING)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(generateRandomMessageId());
        return emailSendHistory;
    }

    public static EmailSendHistory deliverySuccessEmailSendHistory(FormApplication formApplication) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.DELIVERY_SUCCESS)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(generateRandomMessageId());
        return emailSendHistory;
    }

    public static EmailSendHistory bounceRejectEmailSendHistory(FormApplication formApplication) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.BOUNCE_REJECT)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(generateRandomMessageId());
        return emailSendHistory;
    }

    public static EmailSendHistory complaintRejectEmailSendHistory(FormApplication formApplication) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.COMPLAINT_REJECT)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(generateRandomMessageId());
        return emailSendHistory;
    }

    public static EmailSendHistory temporaryFailureEmailSendHistory(FormApplication formApplication) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.TEMPORARY_FAILURE)
                .retryCount(3)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public static EmailSendHistory permanentFailureEmailSendHistory(FormApplication formApplication) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.PERMANENT_FAILURE)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public static EmailSendHistory createWithMessageId(FormApplication formApplication, String messageId) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .status(EmailSendStatus.SENDING)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(messageId);
        return emailSendHistory;
    }

    public static EmailSendHistory deliverySuccessWithFormEmailSendHistory(
            FormApplication formApplication,
            FormEmailSendHistory formEmailSendHistory) {
        EmailSendHistory emailSendHistory = EmailSendHistory.builder()
                .formApplication(formApplication)
                .formEmailSendHistory(formEmailSendHistory)
                .status(EmailSendStatus.DELIVERY_SUCCESS)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
        emailSendHistory.updateMessageTrackingId(generateRandomMessageId());
        return emailSendHistory;
    }

    private static String generateRandomMessageId() {
        return "msg-" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static EmailSendHistory permanentFailureWithFormEmailSendHistory(
            FormApplication formApplication,
            FormEmailSendHistory formEmailSendHistory) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .formEmailSendHistory(formEmailSendHistory)
                .status(EmailSendStatus.PERMANENT_FAILURE)
                .retryCount(0)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public static EmailSendHistory pendingWithFormEmailSendHistory(
            FormApplication formApplication,
            FormEmailSendHistory formEmailSendHistory) {
        return EmailSendHistory.builder()
                .formApplication(formApplication)
                .formEmailSendHistory(formEmailSendHistory)
                .status(EmailSendStatus.PENDING)
                .retryCount(0)
                .sentAt(null)
                .build();
    }
}
