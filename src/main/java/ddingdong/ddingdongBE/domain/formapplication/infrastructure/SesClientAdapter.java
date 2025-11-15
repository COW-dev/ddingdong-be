package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Component
@RequiredArgsConstructor
public class SesClientAdapter implements SesClientPort {

    private final SesClient sesClient;

    @Override
    public SendEmailResponse sendEmail(final SendEmailRequest sendEmailRequest) {
        return sesClient.sendEmail(sendEmailRequest);
    }
}
