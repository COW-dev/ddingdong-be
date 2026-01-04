package ddingdong.ddingdongBE.domain.form.service.event;

import ddingdong.ddingdongBE.email.entity.EmailContent;

public record SendFormResultEmailEvent(
        Long emailSendHistoryId,
        String destinationEmail,
        String destinationName,
        EmailContent emailContent) {
}
