package ddingdong.ddingdongBE.domain.form.infrastructure;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormEmailSender;
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
public class SesFormEmailSender implements FormEmailSender {

    @Value("${cloud.aws.ses.sender-email}")
    private String senderEmail;

    private final SesEmailSender sesEmailSender;

    @Override
    public void sendResult(String destinationName, String destinationEmail, Long emailHistoryId, EmailContent emailContent) {
        SendEmailRequest sendEmailRequest = createSendEmailRequest(destinationName, destinationEmail, emailContent);
        sesEmailSender.sendResult(sendEmailRequest, emailHistoryId);
    }

    private SendEmailRequest createSendEmailRequest(String destinationName, String destinationEmail, EmailContent emailContent) {
        return SendEmailRequest.builder()
                .source(senderEmail)
                .destination(Destination.builder()
                        .toAddresses(destinationEmail)
                        .build())
                .configurationSetName("ddingdong-form-application-result-set")
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
