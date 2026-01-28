package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.EmailException.EmailTemplateNotFoundException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormEmailSendHistoryService {

    private final FormEmailSendHistoryRepository formEmailSendHistoryRepository;

    public FormEmailSendHistory getById(Long id) {
        return formEmailSendHistoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("이메일 전송 기록을 찾을 수 없습니다. (id: " + id + ")"));
    }


    @Transactional
    public FormEmailSendHistory create(
            Form form,
            String title,
            FormApplicationStatus formApplicationStatus,
            String emailContent
    ) {
        return formEmailSendHistoryRepository.save(
                new FormEmailSendHistory(title, formApplicationStatus, emailContent, form));
    }

    public List<FormEmailSendHistory> getAllByFormIdAndFormApplicationStatus(Long formId, FormApplicationStatus status) {
        return formEmailSendHistoryRepository.findAllByFormIdAndFormApplicationStatus(formId, status);
    }

    public FormEmailSendHistory getLatestByFormIdAndApplicationStatus(
            Long formId, FormApplicationStatus status) {
        return formEmailSendHistoryRepository
                .findTopByFormIdAndFormApplicationStatusOrderByIdDesc(formId, status)
                .orElseThrow(EmailTemplateNotFoundException::new);
    }

    public Map<FormApplicationStatus, Long> getLatestIdsByFormIdAndStatuses(
            Long formId, List<FormApplicationStatus> statuses) {
        Map<FormApplicationStatus, Long> result = new EnumMap<>(FormApplicationStatus.class);
        for (FormApplicationStatus status : statuses) {
            formEmailSendHistoryRepository
                    .findTopByFormIdAndFormApplicationStatusOrderByIdDesc(formId, status)
                    .ifPresent(history -> result.put(status, history.getId()));
        }
        return result;
    }
}
