package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.form.entity.FormField;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

    int countByFormField(FormField formField);
    List<FormAnswer> findAllByFormApplication(FormApplication formApplication);
}
