package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public interface FormResultEmailSender {

    void sendResult(String destinationName, String destinationEmail, Long emailHistoryId, EmailContent emailContent);
}
