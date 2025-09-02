package co.com.crediya.security.repository;

import co.com.crediya.security.jwt.manager.JwtAuthenticationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SecurityContextRepositoryTest {

    private JwtAuthenticationManager authManager;
    private SecurityContextRepository contextRepository;
    private ServerWebExchange exchange;

    @BeforeEach
    void setUp() {
        authManager = mock(JwtAuthenticationManager.class);
        contextRepository = new SecurityContextRepository(authManager);
        exchange = mock(ServerWebExchange.class);
    }

    @Test
    void saveShouldReturnEmpty() {
        SecurityContext context = mock(SecurityContext.class);

        StepVerifier.create(contextRepository.save(exchange, context))
                .verifyComplete();
    }

    @Test
    void loadShouldReturnSecurityContextWhenTokenValid() {
        String token = "valid-token";
        Authentication auth = new UsernamePasswordAuthenticationToken("user", null);
        when(exchange.getAttribute("token")).thenReturn(token);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.just(auth));

        StepVerifier.create(contextRepository.load(exchange))
                .expectNextMatches(securityContext ->
                        securityContext.getAuthentication().getName().equals("user"))
                .verifyComplete();

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void loadShouldPropagateErrorWhenAuthenticationFails() {
        String token = "invalid-token";
        when(exchange.getAttribute("token")).thenReturn(token);
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(Mono.error(new RuntimeException("Auth failed")));

        StepVerifier.create(contextRepository.load(exchange))
                .expectErrorMatches(e -> e instanceof RuntimeException &&
                        e.getMessage().equals("Auth failed"))
                .verify();

        verify(authManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
