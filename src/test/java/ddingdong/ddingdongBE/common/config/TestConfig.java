package ddingdong.ddingdongBE.common.config;

import ddingdong.ddingdongBE.common.support.DataInitializer;
import ddingdong.ddingdongBE.email.FakeSesClient;
import ddingdong.ddingdongBE.email.infrastructure.SesClientPort;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@TestConfiguration
@Import(DataInitializer.class)
public class TestConfig {

    @Bean
    @Primary
    public SesClientPort fakeSesClient() {
        return new FakeSesClient();
    }
}
