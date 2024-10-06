package ddingdong.ddingdongBE.common.support;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.documents.controller.AdminDocumentController;
import ddingdong.ddingdongBE.domain.documents.controller.DocumentController;
import ddingdong.ddingdongBE.domain.documents.service.DocumentService;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentService;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.domain.question.controller.AdminQuestionController;
import ddingdong.ddingdongBE.domain.question.controller.QuestionController;
import ddingdong.ddingdongBE.domain.question.service.QuestionService;
import ddingdong.ddingdongBE.domain.scorehistory.controller.AdminScoreHistoryController;
import ddingdong.ddingdongBE.domain.scorehistory.controller.ClubScoreHistoryController;
import ddingdong.ddingdongBE.domain.scorehistory.service.ScoreHistoryService;
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
    AdminQuestionController.class,
    QuestionController.class,
    AdminScoreHistoryController.class,
    ClubScoreHistoryController.class
})
public abstract class WebApiUnitTestSupport {

  @Autowired
  private WebApplicationContext context;
  @Autowired
  protected MockMvc mockMvc;
  @MockBean
  protected DocumentService documentService;
  @MockBean
  protected FacadeDocumentService facadeDocumentService;
  @MockBean
  protected FacadeAdminDocumentService facadeAdminDocumentService;
  @MockBean
  protected FileService fileService;
  @MockBean
  protected FileInformationService fileInformationService;
  @MockBean
  protected QuestionService questionService;
  @MockBean
  protected ClubService clubService;
  @MockBean
  protected ScoreHistoryService scoreHistoryService;

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
