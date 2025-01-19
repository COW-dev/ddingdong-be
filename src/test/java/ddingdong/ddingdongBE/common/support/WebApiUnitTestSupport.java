package ddingdong.ddingdongBE.common.support;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.documents.controller.AdminDocumentController;
import ddingdong.ddingdongBE.domain.documents.controller.DocumentController;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentServiceImpl;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentServiceImpl;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
    AdminDocumentController.class,
    DocumentController.class,
})
public abstract class WebApiUnitTestSupport {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  protected MockMvc mockMvc;
  @MockBean
  protected FacadeDocumentServiceImpl facadeDocumentServiceImpl;
  @MockBean
  protected FacadeAdminDocumentServiceImpl facadeAdminDocumentServiceImpl;
  @MockBean
  protected FileService fileService;
  @MockBean
  protected FileInformationService fileInformationService;
  @MockBean
  protected ClubService clubService;

  @Autowired
  protected ObjectMapper objectMapper;

  protected String toJson(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsString(object);
  }

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(springSecurity())
        .build();
  }

}
