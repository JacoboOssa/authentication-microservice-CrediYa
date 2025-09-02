package co.com.crediya.security.jwt.manager;
import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.security.jwt.manager.JwtAuthenticationManager;
import co.com.crediya.security.jwt.provider.JwtProvider;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

class JwtAuthenticationManagerTest {

    private JwtProvider jwtProvider;
    private JwtAuthenticationManager authManager;

    @BeforeEach
    void setUp() {
        jwtProvider = mock(JwtProvider.class);
        authManager = new JwtAuthenticationManager(jwtProvider);
    }

    @Test
    void shouldAuthenticateValidToken() {
        // Mock Authentication con token
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, "valid-token");

        // Mock Claims
        Claims claims = mock(Claims.class);
        when(claims.get("rol", List.class)).thenReturn(List.of("ROLE_USER", "ROLE_ADMIN"));
        when(claims.getSubject()).thenReturn("user1");

        when(jwtProvider.getClaims("valid-token")).thenReturn(claims);

        Mono<Authentication> result = authManager.authenticate(authentication);

        StepVerifier.create(result)
                .expectNextMatches(auth ->
                        auth.getName().equals("user1") &&
                                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")) &&
                                auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                )
                .verifyComplete();
    }

    @Test
    void shouldThrowBusinessExceptionForInvalidToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(null, "invalid-token");

        when(jwtProvider.getClaims("invalid-token")).thenThrow(new RuntimeException("Invalid"));

        Mono<Authentication> result = authManager.authenticate(authentication);

        StepVerifier.create(result)
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().equals(BusinessException.BAD_CREDENTIALS))
                .verify();
    }
}

