package ddingdong.ddingdongBE.domain.question.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.question.controller.dto.request.GenerateQuestionRequest;
import ddingdong.ddingdongBE.domain.question.controller.dto.request.ModifyQuestionRequest;
import ddingdong.ddingdongBE.domain.question.entity.Question;
import ddingdong.ddingdongBE.support.WebApiTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

class AdminQuestionControllerTest extends WebApiTestSupport {

    @WithMockUser(roles = "ADMIN")
    @DisplayName("question 생성 요청을 수행한다.")
    @Test
    void generateQuestion() throws Exception {
        // given
        GenerateQuestionRequest request = GenerateQuestionRequest.builder()
                .question("testQuestion")
                .reply("testReply").build();

        // when // then
        mockMvc.perform(post("/server/admin/questions")
                        .param("title", request.question())
                        .param("content", request.reply())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(questionService).create(any());
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("questions 조회 요청을 수행한다.")
    @Test
    void getAllDocumentsDocuments() throws Exception {
        //given
        LocalDateTime questionACreatedAt = LocalDateTime.now();
        LocalDateTime questionBCreatedAt = LocalDateTime.now();
        List<Question> foundQuestions = List.of(
                Question.builder().id(1L).question("A").reply("A").createdAt(questionACreatedAt).build(),
                Question.builder().id(2L).question("B").reply("B").createdAt(questionBCreatedAt).build());
        when(questionService.getAll()).thenReturn(foundQuestions);

        //when //then
        mockMvc.perform(get("/server/admin/questions")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(foundQuestions.size())))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].question").value("A"))
                .andExpect(jsonPath("$[0].reply").value("A"))
                .andExpect(jsonPath("$[0].createdAt").value(questionACreatedAt.toString().split("T")[0]))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].question").value("B"))
                .andExpect(jsonPath("$[1].reply").value("B"))
                .andExpect(jsonPath("$[1].createdAt").value(questionBCreatedAt.toString().split("T")[0]));
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("question 자료 수정 요청을 수행한다.")
    @Test
    void modifyQuestion() throws Exception {
        // given
        ModifyQuestionRequest modifyRequest = ModifyQuestionRequest.builder()
                .question("testQuestion")
                .reply("testReply").build();

        // when // then
        mockMvc.perform(patch("/server/admin/questions/{questionId}", 1L)
                        .param("question", modifyRequest.question())
                        .param("reply", modifyRequest.reply())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(questionService).update(anyLong(), any());
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("question 삭제 요청을 수행한다.")
    @Test
    void deleteQuestion() throws Exception {
        //given

        //when //then
        mockMvc.perform(delete("/server/admin/questions/{questionId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(questionService).delete(1L);
    }

}
