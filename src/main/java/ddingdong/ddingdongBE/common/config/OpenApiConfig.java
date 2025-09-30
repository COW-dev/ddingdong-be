package ddingdong.ddingdongBE.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Value("${swagger.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .openapi("3.0.1")
                .components(securityComponents())
                .servers(List.of(new Server().url(serverUrl)));
    }

    private Components securityComponents() {
        return new Components()
                .addSecuritySchemes(
                        "AccessToken",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );
    }

}
