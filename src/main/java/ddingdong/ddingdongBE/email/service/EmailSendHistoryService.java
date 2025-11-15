package ddingdong.ddingdongBE.email.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.formapplication.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailSendHistoryService {

    private final EmailSendHistoryRepository emailSendHistoryRepository;

    @Transactional
    public void trySend(Long emailSendHistoryId) {
        EmailSendHistory emailSendHistory = getById(emailSendHistoryId);
        emailSendHistory.trySend();
    }

    @Transactional
    public void markRetryFailError(Long emailSendHistoryId) {
        EmailSendHistory emailSendHistory = getById(emailSendHistoryId);
        emailSendHistory.markRetryFail();
    }

    @Transactional
    public void markNonRetryableError(Long emailSendHistoryId) {
        EmailSendHistory emailSendHistory = getById(emailSendHistoryId);
        emailSendHistory.markNonRetryFail();
    }

    @Transactional
    public void updateMessageTrackingId(Long emailSendHistoryId, String messageId) {
        EmailSendHistory emailSendHistory = getById(emailSendHistoryId);
        emailSendHistory.updateMessageTrackingId(messageId);
    }

    private EmailSendHistory getById(Long emailSendHistoryId) {
        return emailSendHistoryRepository.findById(emailSendHistoryId)
                .orElseThrow(() -> new ResourceNotFound(
                        "해당 emailSendHistoryId(id = " + emailSendHistoryId + ")로 이메일을 찾을 수 없습니다."));
    }
}
