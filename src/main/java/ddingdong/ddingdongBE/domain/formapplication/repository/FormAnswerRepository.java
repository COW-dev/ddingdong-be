package ddingdong.ddingdongBE.domain.formapplication.repository;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormAnswer;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormAnswerRepository extends JpaRepository<FormAnswer, Long> {

  List<FormAnswer> findAllByFormApplication(FormApplication formApplication);
}
