package ddingdong.ddingdongBE.domain.documents.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ddingdong.ddingdongBE.domain.documents.controller.dto.request.GenerateDocumentRequest;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@WebMvcTest(AdminDocumentController.class)
public class AdminDocumentControllerTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @MockBean
    private FileService fileService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(roles = "ADMIN")
    @DisplayName("document 자료 생성 요청을 수행한다.")
    @Test
    void generate() throws Exception {
        // given
        GenerateDocumentRequest request = GenerateDocumentRequest.builder()
                .title("testTitle")
                .content("testContent").build();
        MockMultipartFile file = new MockMultipartFile("uploadFiles", "test.txt", "text/plain",
                "test content".getBytes());
        when(documentService.create(any()))
                .thenReturn(1L);

        // when // then
        mockMvc.perform(MockMvcRequestBuilders.multipart("/server/admin/documents")
                        .file(file)
                        .param("title", request.getTitle())
                        .param("content", request.getContent())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        verify(fileService).uploadDownloadableFile(anyLong(), anyList(), any(), any());
    }
}
