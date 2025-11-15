package ddingdong.ddingdongBE.domain.formapplication.infrastructure;

import com.google.common.util.concurrent.RateLimiter;
import ddingdong.ddingdongBE.domain.formapplication.entity.EmailContent;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationEmailSender;
import ddingdong.ddingdongBE.email.EmailSendHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.RetryableException;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesFormApplicationEmailSender implements FormApplicationEmailSender {

    private final SesClientPort sesClient;
    private final RateLimiter rateLimiter = RateLimiter.create(12.0); // 초당 12개로 제한
    private final SesEmailRetryPolicy retryPolicy;
    private final EmailSendHistoryService emailSendHistoryService;

    @Value("${cloud.aws.ses.sender-email}")
    private String senderEmail;

    @Retryable(
            retryFor = {RetryableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2.0)
    )
    @Override
    public void sendResult(FormApplication formApplication, EmailContent emailContent, Long emailSendHistoryId) {
        rateLimiter.acquire();
        SendEmailRequest request = createSendEmailRequest(formApplication, emailContent);
        try {
            emailSendHistoryService.trySend(emailSendHistoryId);
            SendEmailResponse sendEmailResponse = sesClient.sendEmail(request);
            emailSendHistoryService.updateMessageTrackingId(emailSendHistoryId, sendEmailResponse.messageId());
        } catch (Exception e) {
            if (retryPolicy.isRetryable(e)) {
                log.error("[Retryable] 이메일 전송 실패 - {} : {}", formApplication.getEmail(), e.getMessage());
                throw RetryableException.create("Retryable AWS SES failure", e);
            }
            log.error("[Non-Retryable] 이메일 전송 실패 - {} : {}", formApplication.getEmail(), e.getMessage());
            nonRetryableError(e, formApplication, emailContent, emailSendHistoryId);
        }
    }

    @Recover
    public void recoverError(RetryableException e, FormApplication formApplication, EmailContent emailContent, Long emailSendHistoryId) {
        log.error("[Retry-Fail] 이메일 전송 실패 - {} : {}", formApplication.getEmail(), e.getMessage());
        emailSendHistoryService.markRetryFailError(emailSendHistoryId);
    }

    private void nonRetryableError(Exception e, FormApplication formApplication, EmailContent emailContent, Long emailSendHistoryId) {
        log.error("[Non-Retryable-Error] 이메일 전송 실패 - {} : {}", formApplication.getEmail(), e.getMessage());
        emailSendHistoryService.markNonRetryableError(emailSendHistoryId);
    }

    private SendEmailRequest createSendEmailRequest(FormApplication formApplication, EmailContent emailContent) {
        return SendEmailRequest.builder()
                .source(senderEmail)
                .destination(Destination.builder()
                        .toAddresses(formApplication.getEmail())
                        .build())
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
