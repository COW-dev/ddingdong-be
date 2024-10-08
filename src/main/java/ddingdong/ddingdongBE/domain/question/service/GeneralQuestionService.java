package ddingdong.ddingdongBE.domain.question.service;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.NO_SUCH_QUESTION;

import ddingdong.ddingdongBE.common.exception.PersistenceException;
import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.repository.QuestionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralQuestionService implements QuestionService {

    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public Long create(Question question) {
        Question createdQuestion = questionRepository.save(question);
        return createdQuestion.getId();
    }

    @Override
    public List<Question> getAll() {
        return questionRepository.findAll();
    }

    @Override
    @Transactional
    public Long update(Long questionId, Question updatedQuestion) {
        Question question = getQuestion(questionId);
        question.updateQuestion(updatedQuestion);
        return question.getId();
    }

    @Override
    public void delete(Long questionId) {
        Question question = getQuestion(questionId);
        questionRepository.delete(question);
    }

    private Question getQuestion(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFound(NO_SUCH_QUESTION.getText()));
    }
}
