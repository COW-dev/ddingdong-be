package ddingdong.ddingdongBE.domain.documents.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GenerateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.ModifyDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.entity.Document;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import ddingdong.ddingdongBE.support.WebApiTestSupport;
import ddingdong.ddingdongBE.support.WithMockAuthenticatedUser;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

public class AdminDocumentControllerTest extends WebApiTestSupport {

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 생성 요청을 수행한다.")
    @Test
    void generateDocument() throws Exception {
        // given
        GenerateDocumentRequest request = GenerateDocumentRequest.builder()
                .title("testTitle")
                .content("testContent").build();
        MockMultipartFile file = new MockMultipartFile("uploadFiles", "test.txt", "text/plain",
                "test content".getBytes());
        when(documentService.create(any()))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(multipart("/server/admin/documents")
                        .file(file)
                        .param("title", request.title())
                        .param("content", request.content())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(fileService).uploadDownloadableFile(anyLong(), anyList(), any(), any());
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("documents 조회 요청을 수행한다.")
    @Test
    void getAllDocumentsDocuments() throws Exception {
        //given
        List<Document> foundDocuments = List.of(
                Document.builder().id(1L).title("A").createdAt(LocalDateTime.now()).build(),
                Document.builder().id(2L).title("B").createdAt(LocalDateTime.now()).build());
        when(documentService.getAll()).thenReturn(foundDocuments);

        //when //then
        mockMvc.perform(get("/server/admin/documents")
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(foundDocuments.size())))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("A"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("B"));
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
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
        mockMvc.perform(get("/server/admin/documents/{documentId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.fileUrls", hasSize(fileResponses.size())))
                .andExpect(jsonPath("$.fileUrls[0].name").value("fileA"))
                .andExpect(jsonPath("$.fileUrls[0].fileUrl").value("fileAUrl"));
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 수정 요청을 수행한다.")
    @Test
    void modify() throws Exception {
        // given
        ModifyDocumentRequest modifyRequest = ModifyDocumentRequest.builder()
                .title("testTitle")
                .content("testContent").build();
        MockMultipartFile file = new MockMultipartFile("uploadFilessymotion-prefix)", "test.txt", "text/plain",
                "test content".getBytes());
        when(documentService.update(1L, modifyRequest.toEntity())).thenReturn(1L);

        // when // then
        mockMvc.perform(multipart("/server/admin/documents/{documentId}", 1L)
                        .file(file)
                        .param("title", modifyRequest.title())
                        .param("content", modifyRequest.content())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(fileService).deleteFile(anyLong(), any(), any());
        verify(fileService).uploadDownloadableFile(anyLong(), any(), any(), any());
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("documents 삭제 요청을 수행한다.")
    @Test
    void deleteDocument() throws Exception {
        //given

        //when //then
        mockMvc.perform(delete("/server/admin/documents/{documentId}", 1L)
                        .with(csrf()))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(documentService).delete(1L);
        verify(fileService).deleteFile(anyLong(), any(), any());
    }
}
