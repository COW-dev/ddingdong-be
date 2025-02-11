package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

    int countByFormField(FormField formField);

    List<FormAnswer> findAllByFormApplication(FormApplication formApplication);

    @Query(value = """
            SELECT fa.value
            FROM form_answer fa
            WHERE fa.field_id = :fieldId
            """, nativeQuery = true)
    List<String> findAllValueByFormField(@Param("fieldId") Long fieldId);
}
