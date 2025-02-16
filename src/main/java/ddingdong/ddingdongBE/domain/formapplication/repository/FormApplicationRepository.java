package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.RecentFormInfo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormApplicationRepository extends JpaRepository<FormApplication, Long> {

    Long countByForm(Form form);

    @Query(value = """
                SELECT f.department, COUNT(f.id) AS count
                FROM form_application f
                WHERE f.form_id = :formId
                GROUP BY f.department
                ORDER BY count DESC
                LIMIT :size
            """, nativeQuery = true)
    List<DepartmentInfo> findTopDepartmentsByFormId(
            @Param("formId") Long formId,
            @Param("size") int size
    );

    @Query(value = """
            SELECT recent_forms.start_date AS date, COUNT(fa.id) AS count
                        FROM (
                            SELECT *
                            FROM form
                            WHERE club_id = :clubId
                            AND start_date <= :date
                            ORDER BY start_date
                            LIMIT :size
                        ) AS recent_forms
                        LEFT JOIN form_application fa
                        ON recent_forms.id = fa.form_id
                        GROUP BY recent_forms.id
            """, nativeQuery = true)
    List<RecentFormInfo> findRecentFormByDateWithApplicationCount(
            @Param("clubId") Long clubId,
            @Param("date") LocalDate date,
            @Param("size") int size
    );

    @Query(value = """
            select fa
            from FormApplication fa
            where fa.form.id = :formId and (fa.status = 'FINAL_PASS' or (fa.status = 'FIRST_PASS' and fa.form.hasInterview = false))
            """)
    List<FormApplication> findAllFinalPassedByFormId(@Param("formId") Long formId);

    List<FormApplication> findAllByForm(Form form);

    List<FormApplication> getAllByFormIdAndStatus(Long formId, FormApplicationStatus status);

}
