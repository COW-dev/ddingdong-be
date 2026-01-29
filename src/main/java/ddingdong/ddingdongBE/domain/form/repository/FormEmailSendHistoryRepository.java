package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormEmailSendHistory;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormEmailSendHistoryRepository extends JpaRepository<FormEmailSendHistory, Long> {

    List<FormEmailSendHistory> findAllByFormId(Long formId);

    List<FormEmailSendHistory> findAllByFormIdAndFormApplicationStatus(
            Long formId, FormApplicationStatus status
    );

    Optional<FormEmailSendHistory> findTopByFormIdAndFormApplicationStatusOrderByIdDesc(
            Long formId,
            FormApplicationStatus formApplicationStatus
    );

    @Query("""
                select fesh
                from FormEmailSendHistory fesh
                where fesh.form.id = :formId
                  and fesh.formApplicationStatus in :statuses
                  and fesh.id in (
                        select max(fesh2.id)
                        from FormEmailSendHistory fesh2
                        where fesh2.form.id = :formId
                          and fesh2.formApplicationStatus in :statuses
                        group by fesh2.formApplicationStatus
                  )
            """)
    List<FormEmailSendHistory> findLatestByFormIdAndStatuses(
            @Param("formId") Long formId,
            @Param("statuses") List<FormApplicationStatus> statuses
    );
}
