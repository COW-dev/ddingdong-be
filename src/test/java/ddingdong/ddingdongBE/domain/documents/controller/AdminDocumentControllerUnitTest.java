package ddingdong.ddingdongBE.domain.documents.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.common.support.WebApiUnitTestSupport;
import ddingdong.ddingdongBE.common.support.WithMockAuthenticatedUser;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.AdminDocumentListQuery;
import ddingdong.ddingdongBE.domain.documents.service.dto.query.AdminDocumentQuery;
import ddingdong.ddingdongBE.file.service.dto.FileResponse;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class AdminDocumentControllerUnitTest extends WebApiUnitTestSupport {

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 생성 요청을 수행한다.")
    @Test
    void createDocument() throws Exception {
        // given
        CreateDocumentCommand command = CreateDocumentCommand.builder()
            .title("testTitle").build();
        MockMultipartFile file = new MockMultipartFile("uploadFiles", "test.txt", "text/plain",
                "test content".getBytes());

        // when // then
        mockMvc.perform(multipart("/server/admin/documents")
                        .file(file)
                        .param("title", command.title())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(facadeAdminDocumentService).create(any());
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("documents 조회 요청을 수행한다.")
    @Test
    void getAdminDocuments() throws Exception {
        //given
        List<AdminDocumentListQuery> queries = List.of(
            AdminDocumentListQuery.builder().id(1L).title("A").createdAt(LocalDate.now()).build(),
            AdminDocumentListQuery.builder().id(2L).title("B").createdAt(LocalDate.now()).build());
        when(facadeAdminDocumentService.getDocuments()).thenReturn(queries);

        //when //then
        mockMvc.perform(get("/server/admin/documents")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(queries.size())))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("B"));
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("documents 상세조회 요청을 수행한다.")
    @Test
    void getAdminDocument() throws Exception {
        //given
        Long documentId = 1L;

        List<FileResponse> fileResponses = List.of(
            FileResponse.builder().name("fileA").fileUrl("fileAUrl").build(),
            FileResponse.builder().name("fileB").fileUrl("fileBUrl").build()
        );

        AdminDocumentQuery query = AdminDocumentQuery.builder()
            .title("title")
            .createdAt(LocalDate.now())
            .fileUrls(fileResponses)
            .build();

        when(facadeAdminDocumentService.getDocument(documentId)).thenReturn(query);

        //when //then
        mockMvc.perform(get("/server/admin/documents/{documentId}", documentId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.fileUrls", hasSize(fileResponses.size())))
                .andExpect(jsonPath("$.fileUrls[0].name").value("fileA"))
                .andExpect(jsonPath("$.fileUrls[0].fileUrl").value("fileAUrl"));
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 수정 요청을 수행한다.")
    @Test
    void modify() throws Exception {
        // given
        UpdateDocumentCommand updateCommand = UpdateDocumentCommand.builder()
            .documentId(1L)
            .title("testTitle")
            .build();
        MockMultipartFile file = new MockMultipartFile("uploadFilessymotion-prefix)", "test.txt", "text/plain",
                "test content".getBytes());
        Long updateId = 1L;
        // when // then
        mockMvc.perform(multipart("/server/admin/documents/{documentId}", updateId)
                        .file(file)
                        .param("title", updateCommand.title())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isOk());

        verify(facadeAdminDocumentService).update(any());
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("documents 삭제 요청을 수행한다.")
    @Test
    void deleteDocument() throws Exception {
        //given
        Long deletedId = 1L;
        //when //then
        mockMvc.perform(delete("/server/admin/documents/{documentId}", deletedId)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(facadeAdminDocumentService).delete(deletedId);
    }
}
