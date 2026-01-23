package ddingdong.ddingdongBE.email.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import ddingdong.ddingdongBE.email.repository.EmailSendHistoryRepository;
import ddingdong.ddingdongBE.email.entity.EmailSendHistories;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

    @Transactional
    public void updateEmailSendStatus(String eventType, String messageId) {
        EmailSendHistory emailSendHistory = emailSendHistoryRepository.findByMessageTrackingId(
                        messageId)
                .orElseThrow(() -> new ResourceNotFound(
                        "해당 messageId(id = " + messageId + ")로 이메일을 찾을 수 없습니다."));
        log.info("이메일(id = {}) 상태 변경 {} -> {}", emailSendHistory.getId(),
                emailSendHistory.getStatus(), eventType);
        emailSendHistory.updateStatusTo(eventType);
    }

    @Transactional
    public EmailSendHistory save(EmailSendHistory emailSendHistory) {
        return emailSendHistoryRepository.save(emailSendHistory);
    }

    private EmailSendHistory getById(Long emailSendHistoryId) {
        return emailSendHistoryRepository.findById(emailSendHistoryId)
                .orElseThrow(() -> new ResourceNotFound(
                        "해당 emailSendHistoryId(id = " + emailSendHistoryId + ")로 이메일을 찾을 수 없습니다."));
    }

    public EmailSendHistories getAllByFormEmailSendHistoryId(Long formEmailSendHistoryId) {
        List<EmailSendHistory> emailSendHistories = emailSendHistoryRepository.findAllByFormEmailSendHistoryId(
                formEmailSendHistoryId);
        return new EmailSendHistories(emailSendHistories);
    }

    public EmailSendHistories getAllByFormEmailSendHistoryIds(List<Long> formEmailSendHistoryIds) {
        List<EmailSendHistory> emailSendHistories = emailSendHistoryRepository.findAllFetchedByFormEmailSendHistoryIdIn(
                formEmailSendHistoryIds);
        return new EmailSendHistories(emailSendHistories);
    }

    public EmailSendHistories findLatestEmailSendHistoryByFormIdAndStatuses(Long formId,
            List<EmailSendStatus> resendTargetStatuses) {
        List<EmailSendHistory> emailSendHistories = emailSendHistoryRepository.findLatestByFormIdAndStatusIn(
                formId, resendTargetStatuses);
        return new EmailSendHistories(emailSendHistories);
    }
}
