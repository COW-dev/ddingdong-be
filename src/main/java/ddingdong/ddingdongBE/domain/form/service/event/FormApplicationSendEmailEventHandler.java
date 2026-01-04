package ddingdong.ddingdongBE.domain.form.service.event;

import ddingdong.ddingdongBE.domain.form.service.FormResultEmailSender;
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

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async("emailAsyncExecutor")
    public void sendBulkResult(SendFormResultEmailEvent event) {
        log.info("지원 결과 이메일 전송 이벤트 핸들링 성공 : {} : {}", event.destinationEmail(), event.destinationName());
        formResultEmailSender.sendResult(
                event.destinationEmail(),
                event.destinationName(),
                event.emailSendHistoryId(),
                event.emailContent()
        );
    }
}
