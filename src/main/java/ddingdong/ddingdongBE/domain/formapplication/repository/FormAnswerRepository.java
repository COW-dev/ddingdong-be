package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

    int countByFormField(FormField formField);

    List<FormAnswer> findAllByFormApplication(FormApplication formApplication);

    @Query(value = """
            SELECT COUNT(*)
            FROM form_answer
            WHERE value LIKE CONCAT('%"', :option, '"%')
            """, nativeQuery = true)
    Integer countAnswerByOption(String option);
}
