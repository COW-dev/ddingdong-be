package ddingdong.ddingdongBE.domain.documents.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import ddingdong.ddingdongBE.support.WebAdaptorTestSupport;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;

class DocumentControllerTest extends WebAdaptorTestSupport {


    @WithMockUser()
    @DisplayName("documents 조회 요청을 수행한다.")
    @Test
    void getAllDocuments() throws Exception {
        //given
        List<Document> foundDocuments = List.of(
                Document.builder().id(1L).title("A").createdAt(LocalDateTime.now()).build(),
                Document.builder().id(2L).title("B").createdAt(LocalDateTime.now()).build());
        when(documentService.getAll()).thenReturn(foundDocuments);

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

    @WithMockUser()
    @DisplayName("documents 상세조회 요청을 수행한다.")
    @Test
    void getDocument() throws Exception {
        //given
        Document document = Document.builder()
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.now()).build();
        when(documentService.getById(1L)).thenReturn(document);

        List<FileResponse> fileResponses = List.of(FileResponse.builder().name("fileA").fileUrl("fileAUrl").build(),
                FileResponse.builder().name("fileB").fileUrl("fileBUrl").build());
        when(fileInformationService.getFileUrls(any())).thenReturn(fileResponses);

        //when //then
        mockMvc.perform(get("/server/documents/{documentId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.fileUrls", hasSize(fileResponses.size())))
                .andExpect(jsonPath("$.fileUrls[0].name").value("fileA"))
                .andExpect(jsonPath("$.fileUrls[0].fileUrl").value("fileAUrl"));
    }
}
