package ddingdong.ddingdongBE.email;

import ddingdong.ddingdongBE.email.infrastructure.SesClientPort;
import java.util.UUID;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

public class FakeSesClient implements SesClientPort {

    @Override
    public SendEmailResponse sendEmail(final SendEmailRequest sendEmailRequest) {
        return SendEmailResponse.builder()
                .messageId(UUID.randomUUID().toString())
                .build();
    }
}
