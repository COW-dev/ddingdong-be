package ddingdong.ddingdongBE.common.config;

import ddingdong.ddingdongBE.common.support.DataInitializer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(DataInitializer.class)
public class TestConfig {
}
