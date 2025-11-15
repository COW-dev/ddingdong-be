package ddingdong.ddingdongBE.domain.formapplication.service;

import ddingdong.ddingdongBE.email.entity.EmailContent;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationEmailSender;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormApplicationEmailService {

    private final FormApplicationEmailSender formApplicationEmailSender;
    private final Executor emailAsyncExecutor;
    private final EmailSendHistoryRepository emailSendHistoryRepository;

    @Transactional
    public void sendBulkResult(List<FormApplication> formApplications, EmailContent emailContent) {
        formApplications.forEach(application -> {
            EmailSendHistory emailSendHistory = emailSendHistoryRepository.save(EmailSendHistory.createPending(application));
            CompletableFuture.runAsync(() -> {
                        formApplicationEmailSender.sendResult(application, emailContent, emailSendHistory.getId());
                    }, emailAsyncExecutor);
                }
        );
    }
}
