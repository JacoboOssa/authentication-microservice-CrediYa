package co.com.crediya.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "Authentication Microservice - CrediYa",
    version = "1.0",
    description = "Microservice for user authentication and authorization"
))
public class SwaggerConfig {
}
