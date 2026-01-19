package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.form.repository.FormEmailSendHistoryRepository;
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
}
