package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import java.time.LocalDateTime;
import java.util.List;


public record EmailSendStatusQuery(
        List<EmailSendStatusInfoQuery> emailSendStatusInfoQueries
) {

    public static EmailSendStatusQuery from(List<EmailSendHistory> emailSendHistories) {
        List<EmailSendStatusInfoQuery> infos = emailSendHistories.stream()
                .map(EmailSendStatusInfoQuery::from)
                .toList();

        return new EmailSendStatusQuery(infos);
    }

    public record EmailSendStatusInfoQuery(
            String name,
            String studentNumber,
            LocalDateTime sendAt,
            EmailSendStatus emailSendStatus,
            FormApplicationStatus formApplicationStatus
    ) {

        public static EmailSendStatusInfoQuery from(EmailSendHistory emailSendHistory) {
            return new EmailSendStatusInfoQuery(
                    emailSendHistory.getFormApplication().getName(),
                    emailSendHistory.getFormApplication().getStudentNumber(),
                    emailSendHistory.getSentAt(),
                    emailSendHistory.getStatus(),
                    emailSendHistory.getFormEmailSendHistory().getFormApplicationStatus()
            );
        }
    }
}
