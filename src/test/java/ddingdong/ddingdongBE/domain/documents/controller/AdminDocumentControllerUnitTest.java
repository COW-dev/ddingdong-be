package ddingdong.ddingdongBE.domain.documents.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.common.support.WebApiUnitTestSupport;
import ddingdong.ddingdongBE.common.support.WithMockAuthenticatedUser;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.CreateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.controller.dto.request.UpdateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentService;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.CreateDocumentCommand;
import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = AdminDocumentController.class)
public class AdminDocumentControllerUnitTest extends WebApiUnitTestSupport {

    @MockitoBean
    protected FacadeAdminDocumentService facadeAdminDocumentService;

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 생성 요청을 수행한다.")
    @Test
    void createDocument() throws Exception {
        // given
        CreateDocumentRequest request = CreateDocumentRequest.builder()
                .title("새로운 문서 제목")
                .fileIds(List.of("1", "2"))
                .build();
        doNothing().when(facadeAdminDocumentService).create(any(CreateDocumentCommand.class));

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.post("/server/admin/documents")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(facadeAdminDocumentService).create(any());
    }

    @WithMockAuthenticatedUser(role = "ADMIN")
    @DisplayName("document 자료 수정 요청을 수행한다.")
    @Test
    void modify() throws Exception {
        // given
        Long updateId = 1L;

        UpdateDocumentRequest request = UpdateDocumentRequest.builder()
                .title("새로운 문서 제목")
                .fileIds(List.of("1", "2"))
                .build();

        doNothing().when(facadeAdminDocumentService).update(any(UpdateDocumentCommand.class));

        // when // then
        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/server/admin/documents/{documentId}", updateId)
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
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
