package ddingdong.ddingdongBE.domain.question.service;

import ddingdong.ddingdongBE.domain.question.service.dto.command.CreateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.command.UpdateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.query.AdminQuestionListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminQuestionServiceImpl implements FacadeAdminQuestionService {

    private final QuestionService questionService;

    @Override
    @Transactional
    public Long create(CreateQuestionCommand command) {
        return questionService.create(command.toEntity());
    }

    @Override
    public List<AdminQuestionListQuery> getAll() {
        return questionService.getAll().stream()
                .map(AdminQuestionListQuery::from)
                .toList();
    }

    @Override
    @Transactional
    public void update(UpdateQuestionCommand command) {
        questionService.update(command.questionId(), command.toEntity());
    }

    @Override
    @Transactional
    public void delete(Long questionId) {
        questionService.delete(questionId);
    }
}
