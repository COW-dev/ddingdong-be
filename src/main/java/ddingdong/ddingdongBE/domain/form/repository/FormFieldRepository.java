package ddingdong.ddingdongBE.domain.form.repository;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.form.repository.dto.FieldListInfo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormFieldRepository extends JpaRepository<FormField, Long> {

    List<FormField> findAllByForm(Form form);

    @Query(value = """
            SELECT f.id AS id, f.question AS question, f.field_type AS type, f.section AS section, COUNT(fa.id) AS count
            FROM (
                    SELECT *
                    FROM form_field field
                    WHERE field.form_id = :formId
            ) AS f
            LEFT JOIN form_answer AS fa
            ON fa.field_id = f.id
            GROUP BY f.id
            ORDER BY f.id
            """, nativeQuery = true)
    List<FieldListInfo> findFieldWithAnswerCountByFormId(@Param("formId") Long formId);

    @Query(value = """
            SELECT *
              FROM form_field f
              WHERE f.form_id = :formId 
                AND f.section = :section
            """, nativeQuery = true)
    List<FormField> findAllByFormAndSection(
            @Param("formId") Long formId,
            @Param("section") String section
    );
}
