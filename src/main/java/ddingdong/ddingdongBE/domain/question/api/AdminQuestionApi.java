package ddingdong.ddingdongBE.domain.question.api;


import ddingdong.ddingdongBE.auth.PrincipalDetails;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.CreateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.UpdateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.response.AdminQuestionListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    void createQuestion(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @ModelAttribute CreateQuestionRequest generateDocumentRequest);

    @Operation(summary = "어드민 FAQ 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @ApiResponse(responseCode = "200", description = "어드민 FAQ 전체 조회 성공",
            content = @Content(schema = @Schema(implementation = AdminQuestionListResponse.class)))
    List<AdminQuestionListResponse> getAllQuestions();

    @Operation(summary = "어드민 FAQ 수정 API")
    @PatchMapping(value = "/{questionId}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void updateQuestion(@PathVariable("questionId") Long questionId,
                        @ModelAttribute UpdateQuestionRequest updateQuestionRequest);

    @Operation(summary = "어드민 FAQ 삭제 API")
    @DeleteMapping("/{questionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    void deleteQuestion(@PathVariable("questionId") Long questionId);
}
