package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormEmailSendHistoryRepository extends JpaRepository<FormEmailSendHistory, Long> {

    Optional<FormEmailSendHistory> findByFormIdAndFormApplicationStatus(Long formId, FormApplicationStatus formApplicationStatus);
}
