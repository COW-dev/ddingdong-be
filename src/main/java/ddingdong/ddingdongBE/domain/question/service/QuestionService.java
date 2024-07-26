package ddingdong.ddingdongBE.domain.question.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_QUESTION;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.repository.QuestionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;

    public Long create(Question question) {
        Question createdQuestion = questionRepository.save(question);
        return createdQuestion.getId();
    }

    @Transactional(readOnly = true)
    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    public Long update(Long questionId, Question updatedDocument) {
        Question question = getQuestion(questionId);
        question.updateQuestion(updatedDocument);
        return question.getId();
    }

    public void delete(Long questionId) {
        Question question = getQuestion(questionId);
        questionRepository.delete(question);
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new NoSuchElementException(NO_SUCH_QUESTION.getText()));
    }
}
