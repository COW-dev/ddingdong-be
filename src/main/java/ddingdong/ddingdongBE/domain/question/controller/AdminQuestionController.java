package ddingdong.ddingdongBE.domain.question.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.question.api.AdminQuestionApi;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.GenerateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.ModifyQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.AdminQuestionResponse;
import ddingdong.ddingdongBE.domain.question.service.QuestionService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminQuestionController implements AdminQuestionApi {

    private final QuestionService questionService;

    @Override
    public void generateQuestion(PrincipalDetails principalDetails, GenerateQuestionRequest generateDocumentRequest) {
        User admin = principalDetails.getUser();
        questionService.create(generateDocumentRequest.toEntity(admin));
    }

    @Override
    public List<AdminQuestionResponse> getAllQuestions() {
        return questionService.getAll().stream()
                .map(AdminQuestionResponse::from)
                .toList();
    }

    @Override
    public void modifyQuestion(Long questionId, ModifyQuestionRequest modifyQuestionRequest) {
        questionService.update(questionId, modifyQuestionRequest.toEntity());
    }

    @Override
    public void deleteQuestion(Long questionId) {
        questionService.delete(questionId);
    }
}
