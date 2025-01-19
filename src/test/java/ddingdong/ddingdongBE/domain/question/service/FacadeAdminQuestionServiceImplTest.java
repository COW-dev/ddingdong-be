package ddingdong.ddingdongBE.domain.question.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.repository.QuestionRepository;
import ddingdong.ddingdongBE.domain.question.service.dto.command.CreateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.command.UpdateQuestionCommand;
import ddingdong.ddingdongBE.domain.question.service.dto.query.AdminQuestionListQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminQuestionServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminQuestionService facadeAdminQuestionService;
    @Autowired
    private QuestionRepository questionRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorMonkey();

    @Test
    @DisplayName("어드민: FAQ 생성")
    void createQuestion() {
        // Given
        CreateQuestionCommand command = CreateQuestionCommand.builder()
                .question("test")
                .reply("test").build();

        // When
        Long createdId = facadeAdminQuestionService.create(command);

        // Then
        Question createdQuestion = questionRepository.findById(createdId).orElseThrow();
        assertThat(createdQuestion.getQuestion()).isEqualTo("test");
        assertThat(createdQuestion.getReply()).isEqualTo("test");
    }

    @Test
    @DisplayName("어드민: FAQ 리스트 조회")
    void getAllQuestions() {
        //given
        List<Question> questions = fixtureMonkey.giveMeBuilder(Question.class)
                .setNull("user")
                .setNotNull("question")
                .setNotNull("reply")
                .set("deletedAt", null)
                .sampleList(5);
        questionRepository.saveAll(questions);

        // When
        List<AdminQuestionListQuery> result = facadeAdminQuestionService.getAll();

        // Then
        assertThat(result).hasSize(5);
    }

    @Test
    @DisplayName("어드민: FAQ 수정")
    void updateQuestion() {
        // Given
        Question beforeQuestion = fixtureMonkey.giveMeBuilder(Question.class)
                .setNull("user")
                .setNotNull("question")
                .setNotNull("reply")
                .sample();
        Question savedBeforeQuestionId = questionRepository.save(beforeQuestion);
        UpdateQuestionCommand command = UpdateQuestionCommand.builder()
                .questionId(savedBeforeQuestionId.getId())
                .question("test")
                .reply("test").build();

        // When
        facadeAdminQuestionService.update(command);

        // Then
        Question updatedQuestion = questionRepository.findById(savedBeforeQuestionId.getId()).orElseThrow();
        assertThat(updatedQuestion.getQuestion()).isEqualTo("test");
        assertThat(updatedQuestion.getReply()).isEqualTo("test");
    }

    @Test
    @DisplayName("어드민: FAQ 삭제")
    void deleteQuestion() {
        // Given
        Question question = fixtureMonkey.giveMeBuilder(Question.class)
                .setNull("user")
                .setNotNull("question")
                .setNotNull("reply")
                .sample();
        Question savedQuestion = questionRepository.save(question);

        // When
        facadeAdminQuestionService.delete(savedQuestion.getId());

        // Then
        assertThat(questionRepository.findById(savedQuestion.getId())).isEmpty();
    }


}
