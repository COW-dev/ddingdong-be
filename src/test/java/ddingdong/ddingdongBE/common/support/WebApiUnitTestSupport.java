package ddingdong.ddingdongBE.common.support;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ddingdong.ddingdongBE.domain.banner.service.FacadeAdminBannerService;
import ddingdong.ddingdongBE.domain.banner.service.FacadeUserBannerService;
import ddingdong.ddingdongBE.domain.club.service.ClubService;
import ddingdong.ddingdongBE.domain.documents.service.FacadeAdminDocumentService;
import ddingdong.ddingdongBE.domain.documents.service.FacadeDocumentService;
import ddingdong.ddingdongBE.domain.fileinformation.service.FileInformationService;
import ddingdong.ddingdongBE.file.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("test")
@WebMvcTest
public abstract class WebApiUnitTestSupport {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected FacadeDocumentService facadeDocumentService;
    @MockitoBean
    protected FacadeAdminDocumentService facadeAdminDocumentService;
    @MockitoBean
    protected FacadeAdminBannerService facadeAdminBannerService;
    @MockitoBean
    protected FacadeUserBannerService facadeUserBannerService;
    @MockitoBean
    protected FileService fileService;
    @MockitoBean
    protected FileInformationService fileInformationService;
    @MockitoBean
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
