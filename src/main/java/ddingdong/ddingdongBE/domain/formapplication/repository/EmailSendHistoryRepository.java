package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendHistoryRepository extends JpaRepository<EmailSendHistory, Long> {

    Optional<EmailSendHistory> findByMessageTrackingId(String messageId);

    List<EmailSendHistory> findAllByFormEmailSendHistoryId(Long formEmailSendHistoryId);
}
