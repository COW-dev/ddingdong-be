package ddingdong.ddingdongBE.domain.formapplication.entity;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public interface FormApplicationEmailSender {

    void sendResult(FormApplication formApplication, EmailContent emailContent, Long emailSendHistoryId);
}
