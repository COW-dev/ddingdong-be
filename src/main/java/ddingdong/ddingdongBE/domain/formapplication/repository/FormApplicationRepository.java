package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.repository.dto.DepartmentInfo;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormApplicationRepository extends JpaRepository<FormApplication, Long> {
    @Query(value = """
            select *
              from form_application f
              where (:currentCursorId = -1 or id < :currentCursorId)
              and f.form_id = :formId
              order by f.id DESC
              limit :size
            """, nativeQuery = true)
    Slice<FormApplication> findPageByFormIdOrderById(
            @Param("formId") Long formId,
            @Param("size") int size,
            @Param("currentCursorId") Long currentCursorId
    );

    Long countByForm(Form form);

    @Query(value = """
                SELECT f.department, COUNT(f.id) AS count
                FROM form_application f
                WHERE f.form_id = :formId
                GROUP BY f.department
                ORDER BY count DESC
                LIMIT 5
            """, nativeQuery = true)
    List<DepartmentInfo> findTopFiveDepartmentsByForm(@Param("formId") Long formId);

    @Query(value = """
            SELECT COUNT(fa.id) AS count
            FROM form_application fa
            JOIN form f ON fa.form_id = f.id
            WHERE f.end_date BETWEEN :startDate AND :endDate
            GROUP BY f.id
            ORDER BY count DESC
            LIMIT 1
            """, nativeQuery = true)
    Integer findMaxApplicationCountByDateRange(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
