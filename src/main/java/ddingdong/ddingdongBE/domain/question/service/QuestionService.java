package ddingdong.ddingdongBE.domain.question.service;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import java.util.List;

public interface QuestionService {

    Long create(Question question);

    List<Question> getAll();

    Long update(Long questionId, Question updatedQuestion);

    void delete(Long questionId);


}
