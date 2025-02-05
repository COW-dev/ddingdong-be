package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

}
