package ddingdong.ddingdongBE.domain.documents.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.support.WebAdaptorTestSupport;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class DocumentControllerTest extends WebAdaptorTestSupport {


    @WithMockUser()
    @DisplayName("documents 조회 요청을 수행한다.")
    @Test
    void getAll() throws Exception {
        //given
        List<Document> foundDocuments = List.of(Document.builder().id(1L).title("A").build(),
                Document.builder().id(2L).title("B").build());
        when(documentService.findAll()).thenReturn(foundDocuments);

        //when //then
        mockMvc.perform(get("/server/documents")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(foundDocuments.size())))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("B"));
    }
}
