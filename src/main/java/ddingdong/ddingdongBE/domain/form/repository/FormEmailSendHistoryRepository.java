package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormEmailSendHistoryRepository extends JpaRepository<FormEmailSendHistory, Long> {

    List<FormEmailSendHistory> getAllByFormId(Long formId);

    Optional<FormEmailSendHistory> findTopByFormIdAndFormApplicationStatusOrderByIdDesc(
            Long FormId,
            FormApplicationStatus formApplicationStatus
    );
}
