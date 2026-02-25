package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public record FormResultSendingEmailInfo(
        Long emailSendHistoryId,
        String destinationEmail,
        String destinationName,
        EmailContent emailContent
) {
}
