package co.com.crediya.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "routes.paths")
public class UserPath {
    private String saveUser;
    private String getAllUsers;
    private String getUserEmailByIdNumber;
    private String logIn;
    private String validate;
    private String getUserByEmail;
}
