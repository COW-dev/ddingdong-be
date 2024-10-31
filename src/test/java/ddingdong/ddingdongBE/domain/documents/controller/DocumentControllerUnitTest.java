package ddingdong.ddingdongBE.domain.documents.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.common.support.WebApiUnitTestSupport;
import ddingdong.ddingdongBE.common.support.WithMockAuthenticatedUser;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.DocumentListPagingQuery;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DocumentControllerUnitTest extends WebApiUnitTestSupport {


    @WithMockAuthenticatedUser
    @DisplayName("documents 조회 요청을 수행한다.")
    @Test
    void getAllDocuments() throws Exception {
        //given
        List<Document> documents = List.of(
            Document.builder().id(1L).title("A").createdAt(LocalDateTime.now()).build(),
            Document.builder().id(2L).title("B").createdAt(LocalDateTime.now()).build()
        );
        int totalPageCount = 10;
        DocumentListPagingQuery queries = DocumentListPagingQuery.of(documents, totalPageCount);
        when(facadeDocumentService.getDocumentList(any())).thenReturn(queries);

        //when //then
        mockMvc.perform(get("/server/documents?page=1&limit=10")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].title").value("B"));
    }

//    @WithMockAuthenticatedUser
//    @DisplayName("documents 상세조회 요청을 수행한다.")
//    @Test
//    void getDocument() throws Exception {
//        //given
//        List<UploadedFileUrlQuery> filurls = List.of(new UploadedFileUrlQuery("originUrl1","cdnUrl1"),
//            new UploadedFileUrlQuery("originUrl2","cdnUrl2"));
//        DocumentQuery query = DocumentQuery.builder()
//            .title("title")
//            .fileUrls(filurls)
//            .createdAt(LocalDate.now()).build();
//        Long documentId = 1L;
//        when(facadeDocumentService.getDocument(documentId)).thenReturn(query);
//
//        //when //then
//        mockMvc.perform(get("/server/documents/{documentId}", 1L)
//                        .with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.title").value("title"))
//                .andExpect(jsonPath("$.fileUrls", hasSize(filurls.size())))
//                .andExpect(jsonPath("$.fileUrls[0].originUrl").value("originUrl1"))
//                .andExpect(jsonPath("$.fileUrls[0].cdnUrl").value("cdnUrl1"));
//    }
}
