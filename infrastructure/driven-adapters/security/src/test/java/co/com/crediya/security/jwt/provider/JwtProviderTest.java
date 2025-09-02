package co.com.crediya.security.jwt.provider;
import co.com.crediya.model.exceptions.JwtException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.lang.reflect.Field;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {


    private JwtProvider jwtProvider;
    private String secret = "U3VwZXJTZWNyZXRLZXlGb3JUZXN0aW5nMTIzNDU2Nzg5MA=="; // Base64URL 256-bit

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(secret,3600000);
    }

    @Test
    void shouldGenerateToken() {
        User user = new User();
        Rol rol = new Rol();
        rol.setName("ROLE_USER");
        user.setRole(rol);
        user.setEmail("test@example.com");

        StepVerifier.create(jwtProvider.generateToken(user))
                .expectNextMatches(token -> token != null && !token.isEmpty())
                .verifyComplete();
    }

    @Test
    void shouldValidateToken() {
        User user = new User();
        Rol rol = new Rol();
        rol.setName("ROLE_USER");
        user.setRole(rol);
        user.setEmail("test@example.com");

        String token = jwtProvider.generateToken(user).block();

        StepVerifier.create(jwtProvider.validate(token))
                .expectNextMatches(u ->
                        u.getEmail().equals("test@example.com") &&
                                u.getRole() != null &&
                                u.getRole().getName().equals("ROLE_USER"))
                .verifyComplete();
    }

    @Test
    void shouldGetClaimsAndSubject() {
        User user = new User();
        Rol rol = new Rol();
        rol.setName("ROLE_USER");
        user.setRole(rol);
        user.setEmail("test@example.com");

        String token = jwtProvider.generateToken(user).block();

        Claims claims = jwtProvider.getClaims(token);
        String subject = jwtProvider.getSubject(token);

        assertEquals("test@example.com", claims.getSubject());
        assertEquals("test@example.com", subject);
    }

    @Test
    void shouldThrowExceptionForMalformedToken() {
        String invalidToken = "malformed.token.here";

        StepVerifier.create(jwtProvider.validate(invalidToken))
                .expectErrorMatches(e -> e instanceof JwtException &&
                        e.getMessage().equals(JwtException.TOKEN_MALFORMED))
                .verify();
    }

    @Test
    void shouldHandleExpiredToken() {
        User user = new User();
        user.setEmail("test@example.com");

        JwtProvider expiredProvider = new JwtProvider(secret, -1000);

        String expiredToken = expiredProvider.generateToken(user).block();

        StepVerifier.create(jwtProvider.validate(expiredToken))
                .expectErrorMatches(e -> e instanceof JwtException &&
                        e.getMessage().equals(JwtException.TOKEN_EXPIRED))
                .verify();
    }
}
