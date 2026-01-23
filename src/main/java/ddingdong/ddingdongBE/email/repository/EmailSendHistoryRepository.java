package ddingdong.ddingdongBE.email.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmailSendHistoryRepository extends JpaRepository<EmailSendHistory, Long> {

    Optional<EmailSendHistory> findByMessageTrackingId(String messageId);

    List<EmailSendHistory> findAllByFormEmailSendHistoryId(Long formEmailSendHistoryId);

    @EntityGraph(attributePaths = {"formApplication"})
    List<EmailSendHistory> findAllFetchedByFormEmailSendHistoryIdIn(
            List<Long> formEmailSendHistoryIds);

    @EntityGraph(attributePaths = {"formApplication"})
    @Query("""
                select esh
                from EmailSendHistory esh
                where esh.id in (
                    select max(esh2.id)
                    from EmailSendHistory esh2
                    where esh2.formApplication.form.id = ?1
                    group by esh2.formApplication.id
                )
                and esh.status in ?2
            """)
    List<EmailSendHistory> findLatestByFormIdAndStatusIn(Long formId,
            List<EmailSendStatus> statuses);

}
