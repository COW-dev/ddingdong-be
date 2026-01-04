package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public interface FormResultEmailSender {

    void sendResult(String destinationEmail, String destinationName, Long emailHistoryId, EmailContent emailContent);
}
