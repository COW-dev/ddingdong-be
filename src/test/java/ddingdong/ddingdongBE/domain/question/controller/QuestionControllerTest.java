package ddingdong.ddingdongBE.domain.question.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.support.WebApiTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class QuestionControllerTest extends WebApiTestSupport {

    @WithMockUser()
    @DisplayName("questions 조회 요청을 수행한다.")
    @Test
    void getAllDocumentsDocuments() throws Exception {
        //given
        List<Question> foundQuestions = List.of(
                Question.builder().id(1L).question("A").reply("A").createdAt(LocalDateTime.now()).build(),
                Question.builder().id(2L).question("B").reply("B").createdAt(LocalDateTime.now()).build());
        when(questionService.getAll()).thenReturn(foundQuestions);

        //when //then
        mockMvc.perform(get("/server/questions")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(foundQuestions.size())))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].question").value("A"))
                .andExpect(jsonPath("$[0].reply").value("A"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].question").value("B"))
                .andExpect(jsonPath("$[1].reply").value("B"));
    }

}
