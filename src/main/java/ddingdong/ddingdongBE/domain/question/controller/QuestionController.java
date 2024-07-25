package ddingdong.ddingdongBE.domain.question.controller;

import ddingdong.ddingdongBE.domain.question.api.QuestionApi;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.QuestionResponse;
import ddingdong.ddingdongBE.domain.question.service.QuestionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController implements QuestionApi {

    private final QuestionService questionService;


    @Override
    public List<QuestionResponse> getAllQuestions() {
        return questionService.getAll().stream()
                .map(QuestionResponse::from)
                .toList();
    }
}
