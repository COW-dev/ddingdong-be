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
                            try {
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
                            } catch (Exception e) {
                                log.error("Failed to send email to {}: {}", application.getEmail(), e.getMessage());
                                throw new RuntimeException("이메일 전송에 실패했습니다: " + application.getEmail(), e);
                            }
                        }))
                        .toArray(CompletableFuture[]::new)
        );
    }
}
