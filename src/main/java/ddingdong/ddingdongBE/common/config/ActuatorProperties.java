package ddingdong.ddingdongBE.common.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "actuator")
@Getter
public class ActuatorProperties {

    private String user;
    private String password;
    private String roleName;
}
