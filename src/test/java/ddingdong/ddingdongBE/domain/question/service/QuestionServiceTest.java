package ddingdong.ddingdongBE.domain.question.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.repository.QuestionRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QuestionServiceTest extends TestContainerSupport {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionRepository questionRepository;


    @DisplayName("document(자료)를 생성한다.")
    @Test
    void create() {
        //given
        Question document = Question.builder()
                .question("test")
                .reply("test")
                .build();

        //when
        Long createdQuestionId = questionService.create(document);

        //then
        Optional<Question> foundDocument = questionRepository.findById(createdQuestionId);
        assertThat(foundDocument.isPresent()).isTrue();
        assertThat(foundDocument.get().getId()).isEqualTo(createdQuestionId);
    }

}
