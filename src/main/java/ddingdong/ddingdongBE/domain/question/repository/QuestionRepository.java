package ddingdong.ddingdongBE.domain.question.repository;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
