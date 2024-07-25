package ddingdong.ddingdongBE.domain.question.api;


import ddingdong.ddingdongBE.domain.question.controller.dto.request.GenerateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.ModifyQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.AdminQuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "FAQ - Admin", description = "FAQ Admin API")
@RequestMapping("/server/admin/questions")
public interface AdminQuestionApi {

    @Operation(summary = "어드민 FAQ 업로드 API")
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    void generateQuestion(@ModelAttribute GenerateQuestionRequest generateDocumentRequest);

    @Operation(summary = "어드민 FAQ 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    List<AdminQuestionResponse> getAllQuestions();

    @Operation(summary = "어드민 FAQ 수정 API")
    @PatchMapping(value = "/{questionId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void modifyQuestion(@PathVariable Long questionId,
                        @ModelAttribute ModifyQuestionRequest modifyQuestionRequest);

    @Operation(summary = "어드민 FAQ 삭제 API")
    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteQuestion(@PathVariable Long questionId);
}
