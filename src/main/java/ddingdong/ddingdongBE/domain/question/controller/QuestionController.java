package ddingdong.ddingdongBE.domain.question.controller;

import ddingdong.ddingdongBE.domain.question.api.QuestionApi;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.QuestionListResponse;
import ddingdong.ddingdongBE.domain.question.service.FacadeUserQuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final FacadeUserQuestionService facadeUserQuestionService;

    @Override
    public List<QuestionListResponse> getAllQuestions() {
        return facadeUserQuestionService.getAll().stream()
                .map(QuestionListResponse::from)
                .toList();
    }
}
