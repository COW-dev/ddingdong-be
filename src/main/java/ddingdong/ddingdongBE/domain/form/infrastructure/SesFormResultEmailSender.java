package ddingdong.ddingdongBE.domain.form.infrastructure;

import ddingdong.ddingdongBE.domain.form.service.FormResultEmailSender;
import ddingdong.ddingdongBE.email.entity.EmailContent;
import ddingdong.ddingdongBE.email.infrastructure.SesEmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@RequiredArgsConstructor
@Component
public class SesFormResultEmailSender implements FormResultEmailSender {

    @Value("${cloud.aws.ses.sender-email}")
    private String senderEmail;

    @Value("${cloud.aws.ses.configuration-set-name}")
    private String configurationSetName;

    private final SesEmailSender sesEmailSender;

    @Override
    public void sendResult(String destinationEmail, String destinationName, Long emailHistoryId, EmailContent emailContent) {
        SendEmailRequest sendEmailRequest = createSendEmailRequest(destinationEmail, destinationName, emailContent);
        sesEmailSender.sendResult(sendEmailRequest, emailHistoryId);
    }

    private SendEmailRequest createSendEmailRequest( String destinationEmail, String destinationName, EmailContent emailContent) {
        return SendEmailRequest.builder()
                .source(senderEmail)
                .destination(Destination.builder()
                        .toAddresses(destinationEmail)
                        .build())
                .configurationSetName(configurationSetName)
                .message(Message.builder()
                        .subject(Content.builder()
                                .charset("UTF-8")
                                .data(emailContent.subject())
                                .build())
                        .body(Body.builder()
                                .html(Content.builder()
                                        .charset("UTF-8")
                                        .data(emailContent.htmlContent()
                                                .replace("{지원자명}", destinationName))
                                        .build())
                                .text(Content.builder()
                                        .charset("UTF-8")
                                        .data(emailContent.textContent()
                                                .replace("{지원자명}", destinationName))
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
