package ddingdong.ddingdongBE.email;

import com.google.common.util.concurrent.RateLimiter;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.email.dto.EmailContent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.LimitExceededException;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class SesEmailService {

    private final SesClient sesClient;
    private final RateLimiter rateLimiter = RateLimiter.create(8.0); // 초당 8개로 제한

    @Value("${cloud.aws.ses.sender-email}")
    private String senderEmail;

    @Async
    public CompletableFuture<Void> sendBulkResultEmails(List<FormApplication> formApplications,
            EmailContent emailContent) {
        return CompletableFuture.allOf(
                formApplications.stream()
                        .map(application -> CompletableFuture.runAsync(() -> {
                            int maxRetries = 5;
                            long retryDelayMs = 200; // 200ms 고정 대기

                            for (int attempt = 0; attempt <= maxRetries; attempt++) {
                                try {
                                    sendEmail(emailContent, application);
                                    break;
                                } catch (LimitExceededException e) {
                                    try {
                                        retryFixedInterval(application, e, attempt, maxRetries, retryDelayMs);
                                    } catch (InterruptedException ie) {
                                        Thread.currentThread().interrupt();
                                        log.error("이메일 전송 중 인터럽트 발생 - {}", application.getEmail());
                                        throw new RuntimeException("이메일 전송 중 인터럽트 발생: " + application.getEmail(), ie);
                                    }
                                } catch (Exception e) {
                                    log.error("이메일 전송 실패 - {}: {}", application.getEmail(), e.getMessage());
                                    throw new RuntimeException("이메일 전송에 실패했습니다: " + application.getEmail(), e);
                                }
                            }
                        }))
                        .toArray(CompletableFuture[]::new)
        );
    }

    private void sendEmail(EmailContent emailContent, FormApplication application) {
        rateLimiter.acquire();
        SendEmailRequest request = SendEmailRequest.builder()
                .source(senderEmail)
                .destination(Destination.builder()
                        .toAddresses(application.getEmail())
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
                                                .replace("{지원자명}", application.getName()))
                                        .build())
                                .text(Content.builder()
                                        .charset("UTF-8")
                                        .data(emailContent.textContent()
                                                .replace("{지원자명}", application.getName()))
                                        .build())
                                .build())
                        .build())
                .build();

        sesClient.sendEmail(request);
    }

    private void retryFixedInterval(FormApplication application,
            LimitExceededException e,
            int attempt,
            int maxRetries,
            long retryDelayMs) throws InterruptedException {
        if (attempt == maxRetries) {
            log.error("최대 재시도 횟수 초과. 이메일 전송 실패 - {}", application.getEmail());
            throw new RuntimeException("이메일 전송 최대 재시도 횟수 초과: " + application.getEmail(), e);
        }
        log.warn("Rate limit 발생. {}ms 후 재시도 ({}/{}): {}",
                retryDelayMs, attempt + 1, maxRetries, application.getEmail());

        // InterruptedException을 다시 던져서 호출자가 처리할 수 있게 함
        Thread.sleep(retryDelayMs);
    }
}
