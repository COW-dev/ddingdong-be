package ddingdong.ddingdongBE.email.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.email.entity.EmailSendHistory;
import ddingdong.ddingdongBE.email.entity.EmailSendStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
                    where esh2.formApplication.form.id = :formId
                    group by esh2.formApplication.id
                )
                and esh.status in :statuses
                and esh.formEmailSendHistory.formApplicationStatus = :applicationStatus
            """)
    List<EmailSendHistory> findLatestByFormIdAndStatusesAndApplicationStatus(
            @Param("formId") Long formId,
            @Param("statuses") List<EmailSendStatus> statuses,
            @Param("applicationStatus") FormApplicationStatus applicationStatus
    );

    @Query("""
                select esh
                from EmailSendHistory esh
                join fetch esh.formEmailSendHistory fesh
                where esh.id in (
                    select max(esh2.id)
                    from EmailSendHistory esh2
                    join esh2.formEmailSendHistory fesh2
                    where fesh2.form.id = :formId
                      and fesh2.formApplicationStatus in :statuses
                    group by esh2.formApplication.id, fesh2.formApplicationStatus
                )
            """)
    List<EmailSendHistory> findLatestPerApplicationByFormIdAndApplicationStatuses(
            @Param("formId") Long formId,
            @Param("statuses") List<FormApplicationStatus> statuses
    );

}
