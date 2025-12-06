package ddingdong.ddingdongBE.domain.form.service.event;

import ddingdong.ddingdongBE.domain.form.entity.FormResultSendingEmailInfo;
import ddingdong.ddingdongBE.domain.form.service.FormResultEmailSender;
import ddingdong.ddingdongBE.email.service.EmailSendHistoryService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormApplicationSendEmailEventHandler {

    private final FormResultEmailSender formResultEmailSender;
    private final EmailSendHistoryService emailSendHistoryService;
    private final Executor emailAsyncExecutor;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("generalAsyncExecutor")
    public void sendBulkResult(SendFormResultEvent event) {
        event.formResultSendingEmailInfos()
                .forEach(info -> CompletableFuture.runAsync(
                                () -> sendEmail(info), emailAsyncExecutor
                        )
                );
    }

    private void sendEmail(final FormResultSendingEmailInfo info) {
        try {
            log.info("지원 결과 이메일 전송 이벤트 핸들링 성공 : {} : {}", info.destinationEmail(), info.destinationName());
            formResultEmailSender.sendResult(info);
        } catch (Exception e) {
            log.error("이메일 전송 실패 : {} : {}", info.destinationEmail(), info.destinationName(), e);
            emailSendHistoryService.markNonRetryableError(info.emailSendHistoryId());
        }
    }
}
