package ddingdong.ddingdongBE.domain.question.service;

import ddingdong.ddingdongBE.domain.question.service.dto.query.AdminQuestionListQuery;
import ddingdong.ddingdongBE.domain.question.service.dto.query.UserQuestionListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeUserQuestionServiceImpl implements FacadeUserQuestionService {

    private final QuestionService questionService;

    @Override
    public List<UserQuestionListQuery> getAll() {
        return questionService.getAll().stream()
                .map(UserQuestionListQuery::from)
                .toList();
    }
}
