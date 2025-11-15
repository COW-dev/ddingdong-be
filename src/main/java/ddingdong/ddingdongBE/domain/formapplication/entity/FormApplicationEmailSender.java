package ddingdong.ddingdongBE.domain.formapplication.entity;

public interface FormApplicationEmailSender {

    void sendResult(FormApplication formApplication, EmailContent emailContent, Long emailSendHistoryId);
}
