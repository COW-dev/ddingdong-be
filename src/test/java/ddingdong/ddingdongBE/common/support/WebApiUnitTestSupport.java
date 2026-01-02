package ddingdong.ddingdongBE.common.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
public abstract class WebApiUnitTestSupport {

//    @Autowired
//    private WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    protected String toJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

//    @BeforeEach
//    void setUp() { TODO: 추후 컨벤션 정의 후 적용
//        mockMvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }
}
