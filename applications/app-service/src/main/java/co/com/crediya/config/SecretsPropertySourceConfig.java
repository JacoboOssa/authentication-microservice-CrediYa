package co.com.crediya.config;

import co.com.bancolombia.secretsmanager.api.GenericManagerAsync;
import co.com.bancolombia.secretsmanager.api.exceptions.SecretException;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

@Configuration
public class SecretsPropertySourceConfig {

    private final GenericManagerAsync secretsManager;
    private final String secretName;

    public SecretsPropertySourceConfig(GenericManagerAsync secretsManager, @Value("${aws.secretName}") String secretName) {
        this.secretsManager = secretsManager;
        this.secretName = secretName;
    }

    @Bean
    public PropertySource<?> awsSecretsPropertySource() throws SecretException {
        Map<String, Object> secrets = secretsManager
                .getSecret(secretName, Map.class)
                .block();

        if (secrets == null) {
            throw new RuntimeException("No se pudo cargar el secret 'auth_secrets' de AWS Secrets Manager");
        }

        return new MapPropertySource("auth-secrets", secrets);
    }
}
