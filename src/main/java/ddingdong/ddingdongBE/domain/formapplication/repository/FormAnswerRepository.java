package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

    int countByFormField(FormField formField);
}
