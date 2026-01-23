package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.List;
import java.util.Optional;
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
    public FormEmailSendHistory create(Form form, FormApplicationStatus formApplicationStatus,
            String emailContent) {
        return formEmailSendHistoryRepository.save(
                new FormEmailSendHistory(formApplicationStatus, emailContent, form));
    }

    public List<FormEmailSendHistory> getAllByFormId(Long formId) {
        return formEmailSendHistoryRepository.getAllByFormId(formId);
    }

    public Optional<FormEmailSendHistory> findLatestByFormIdAndApplicationStatus(
            Long formId, FormApplicationStatus formApplicationStatus) {
        return formEmailSendHistoryRepository.findTopByFormIdAndFormApplicationStatusOrderByIdDesc(
                formId, formApplicationStatus);
    }
}
