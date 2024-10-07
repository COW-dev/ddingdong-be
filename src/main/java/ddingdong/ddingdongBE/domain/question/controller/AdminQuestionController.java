package ddingdong.ddingdongBE.domain.question.controller;

import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.question.api.AdminQuestionApi;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.GenerateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.UpdateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.AdminQuestionListResponse;
import ddingdong.ddingdongBE.domain.question.service.FacadeAdminQuestionService;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminQuestionController implements AdminQuestionApi {

    private final FacadeAdminQuestionService facadeAdminQuestionService;

    @Override
    public void generateQuestion(PrincipalDetails principalDetails, GenerateQuestionRequest generateDocumentRequest) {
        User admin = principalDetails.getUser();
        facadeAdminQuestionService.create(generateDocumentRequest.toCommand(admin));
    }

    @Override
    public List<AdminQuestionListResponse> getAllQuestions() {
        return facadeAdminQuestionService.getAll().stream()
                .map(AdminQuestionListResponse::from)
                .toList();
    }

    @Override
    public void modifyQuestion(Long questionId, UpdateQuestionRequest updateQuestionRequest) {
        facadeAdminQuestionService.update(updateQuestionRequest.toCommand(questionId));
    }

    @Override
    public void deleteQuestion(Long questionId) {
        facadeAdminQuestionService.delete(questionId);
    }
}
