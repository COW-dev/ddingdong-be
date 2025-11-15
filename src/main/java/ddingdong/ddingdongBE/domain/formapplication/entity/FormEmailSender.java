package ddingdong.ddingdongBE.domain.formapplication.entity;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public interface FormEmailSender {

    void sendResult(String destinationName, String destinationEmail, Long emailHistoryId, EmailContent emailContent);
}
