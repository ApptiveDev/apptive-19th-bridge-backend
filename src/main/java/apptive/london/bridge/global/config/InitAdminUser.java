package apptive.london.bridge.global.config;

import apptive.london.bridge.domain.user.entity.User;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "initial-user")
@Data
public class InitAdminUser {
    private String email;
    private String password;

}
