package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.EmailSendHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendHistoryRepository extends JpaRepository<EmailSendHistory, Long> {

}
