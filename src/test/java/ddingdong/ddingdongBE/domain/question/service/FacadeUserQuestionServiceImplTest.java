package ddingdong.ddingdongBE.domain.question.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.domain.question.repository.QuestionRepository;
import ddingdong.ddingdongBE.domain.question.service.dto.query.UserQuestionListQuery;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeUserQuestionServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeUserQuestionService facadeUserQuestionService;
    @Autowired
    private QuestionRepository questionRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getBuilderIntrospectorMonkey();

    @Test
    @DisplayName("유저: FAQ 리스트 조회")
    void getAllQuestions() {
        //given
        List<Question> questions = fixtureMonkey.giveMeBuilder(Question.class)
                .setNull("user")
                .setNotNull("question")
                .setNotNull("reply")
                .sampleList(5);
        questionRepository.saveAll(questions);

        // When
        List<UserQuestionListQuery> result = facadeUserQuestionService.getAll();

        // Then
        assertThat(result).hasSize(5);
    }

}
