package ddingdong.ddingdongBE.domain.question.api;


import ddingdong.ddingdongBE.domain.question.controller.dto.response.QuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "FAQ", description = "FAQ API")
@RequestMapping("/server/questions")
public interface QuestionApi {

    @Operation(summary = "FAQ 목록 조회 API")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    List<QuestionResponse> getAllQuestions();

}
