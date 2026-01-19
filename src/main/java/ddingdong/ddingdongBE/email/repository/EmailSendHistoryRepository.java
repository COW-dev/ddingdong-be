package ddingdong.ddingdongBE.email.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailSendHistoryRepository extends JpaRepository<EmailSendHistory, Long> {

    Optional<EmailSendHistory> findByMessageTrackingId(String messageId);

    List<EmailSendHistory> findAllByFormEmailSendHistoryId(Long formEmailSendHistoryId);

    @EntityGraph(attributePaths = {"formApplication"})
    List<EmailSendHistory> findAllFetchedByFormEmailSendHistoryIdIn(List<Long> formEmailSendHistoryIds);
}
