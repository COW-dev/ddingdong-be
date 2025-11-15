package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

public interface SesClientPort {

    SendEmailResponse sendEmail(SendEmailRequest sendEmailRequest);
}
