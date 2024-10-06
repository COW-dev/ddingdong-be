package ddingdong.ddingdongBE.domain.question.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.question.api.AdminQuestionApi;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.GenerateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.ModifyQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.AdminQuestionListResponse;
import ddingdong.ddingdongBE.domain.question.service.GeneralQuestionService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminQuestionController implements AdminQuestionApi {

    private final GeneralQuestionService generalQuestionService;

    @Override
    public void generateQuestion(PrincipalDetails principalDetails, GenerateQuestionRequest generateDocumentRequest) {
        User admin = principalDetails.getUser();
        generalQuestionService.create(generateDocumentRequest.toEntity(admin));
    }

    @Override
    public List<AdminQuestionListResponse> getAllQuestions() {
        return generalQuestionService.getAll().stream()
                .map(AdminQuestionListResponse::from)
                .toList();
    }

    @Override
    public void modifyQuestion(Long questionId, ModifyQuestionRequest modifyQuestionRequest) {
        generalQuestionService.update(questionId, modifyQuestionRequest.toEntity());
    }

    @Override
    public void deleteQuestion(Long questionId) {
        generalQuestionService.delete(questionId);
    }
}
