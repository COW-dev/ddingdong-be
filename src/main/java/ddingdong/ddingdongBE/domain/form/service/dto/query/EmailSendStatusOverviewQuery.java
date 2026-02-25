package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.time.LocalDateTime;
import java.util.List;

public record EmailSendStatusOverviewQuery(
        List<EmailSendStatusOverviewInfoQuery> emailSendStatusOverviewInfoQueries
) {
    public static EmailSendStatusOverviewQuery of(
            List<EmailSendStatusOverviewInfoQuery> infoQueries) {
        return new EmailSendStatusOverviewQuery(infoQueries);
    }

    public record EmailSendStatusOverviewInfoQuery(
            FormApplicationStatus formApplicationStatus,
            LocalDateTime lastSentAt,
            int successCount,
            int failCount
    ) {

        public static EmailSendStatusOverviewInfoQuery of(
                FormApplicationStatus status,
                LocalDateTime lastSentAt,
                int successCount,
                int failCount
        ) {
            return new EmailSendStatusOverviewInfoQuery(status, lastSentAt, successCount,
                    failCount);
        }

        public static EmailSendStatusOverviewInfoQuery empty(FormApplicationStatus status) {
            return new EmailSendStatusOverviewInfoQuery(status, null, 0, 0);
        }
    }
}
