package ddingdong.ddingdongBE.email.infrastructure;

import com.google.common.util.concurrent.RateLimiter;
import ddingdong.ddingdongBE.email.service.EmailSendHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.RetryableException;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendEmailResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SesEmailSender {

    private final SesClientPort sesClient;
    private final RateLimiter rateLimiter = RateLimiter.create(12.0); // 초당 12개로 제한
    private final SesEmailRetryPolicy retryPolicy;
    private final EmailSendHistoryService emailSendHistoryService;

    @Retryable(
            retryFor = {RetryableException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 200, multiplier = 2.0)
    )
    public void sendResult(SendEmailRequest request, Long emailSendHistoryId) {
        rateLimiter.acquire();
        try {
            emailSendHistoryService.trySend(emailSendHistoryId);
            SendEmailResponse sendEmailResponse = sesClient.sendEmail(request);
            emailSendHistoryService.updateMessageTrackingId(emailSendHistoryId, sendEmailResponse.messageId());
        } catch (Exception e) {
            if (retryPolicy.isRetryable(e)) {
                log.error("[Retryable] 이메일 전송 실패 - {} : {}", emailSendHistoryId, e.getMessage());
                throw RetryableException.create("Retryable AWS SES failure", e);
            }
            log.error("[Non-Retryable] 이메일 전송 실패 - {} : {}", emailSendHistoryId, e.getMessage());
            nonRetryableError(e, emailSendHistoryId);
        }
    }

    @Recover
    public void recoverError(RetryableException e, SendEmailRequest request, Long emailSendHistoryId) {
        log.error("[Retry-Fail] 이메일 전송 실패 - {} : {}", emailSendHistoryId, e.getMessage());
        emailSendHistoryService.markRetryFailError(emailSendHistoryId);
    }

    private void nonRetryableError(Exception e, Long emailSendHistoryId) {
        log.error("[Non-Retryable-Error] 이메일 전송 실패 - {} : {}", emailSendHistoryId, e.getMessage());
        emailSendHistoryService.markNonRetryableError(emailSendHistoryId);
    }
}
