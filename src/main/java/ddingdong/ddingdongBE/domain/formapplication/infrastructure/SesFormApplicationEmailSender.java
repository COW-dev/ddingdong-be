package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationEmailSender;
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
public class SesFormApplicationEmailSender implements FormApplicationEmailSender {

    @Value("${cloud.aws.ses.sender-email}")
    private String senderEmail;

    private final SesEmailSender sesEmailSender;

    @Override
    public void sendResult(final FormApplication formApplication, final EmailContent emailContent,
            final Long emailSendHistoryId) {
        SendEmailRequest sendEmailRequest = createSendEmailRequest(formApplication, emailContent);
        sesEmailSender.sendResult(sendEmailRequest, emailSendHistoryId);
    }

    private SendEmailRequest createSendEmailRequest(FormApplication formApplication, EmailContent emailContent) {
        return SendEmailRequest.builder()
                .source(senderEmail)
                .destination(Destination.builder()
                        .toAddresses(formApplication.getEmail())
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
                                                .replace("{지원자명}", formApplication.getName()))
                                        .build())
                                .text(Content.builder()
                                        .charset("UTF-8")
                                        .data(emailContent.textContent()
                                                .replace("{지원자명}", formApplication.getName()))
                                        .build())
                                .build())
                        .build())
                .build();
    }
}
