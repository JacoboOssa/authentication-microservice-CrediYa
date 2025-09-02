package co.com.crediya.security.config;

import co.com.crediya.security.jwt.filter.JwtFilter;
import co.com.crediya.security.repository.SecurityContextRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private SecurityContextRepository mockRepo;
    private JwtFilter mockFilter;

    @BeforeEach
    void setUp() {
        mockRepo = Mockito.mock(SecurityContextRepository.class);
        mockFilter = Mockito.mock(JwtFilter.class);
        securityConfig = new SecurityConfig(mockRepo);
    }

    @Test
    void shouldBuildSecurityWebFilterChain() {
        ServerHttpSecurity http = ServerHttpSecurity.http();
        SecurityWebFilterChain chain = securityConfig.filterChain(http, mockFilter);

        assertThat(chain).isNotNull();
    }
}
