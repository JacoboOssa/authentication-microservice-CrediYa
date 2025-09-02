package co.com.crediya.security.repository;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.helper.UserEntityMapper;
import co.com.crediya.security.jwt.provider.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AuthReactiveRepositoryAdapterTest {

    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;
    private UserEntityMapper userEntityMapper;
    private AuthReactiveRepositoryAdapter authRepo;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        jwtProvider = mock(JwtProvider.class);
        userEntityMapper = mock(UserEntityMapper.class);

        authRepo = new AuthReactiveRepositoryAdapter(passwordEncoder, jwtProvider, userEntityMapper);
    }

    @Test
    void shouldHashPassword() {
        String raw = "password123";
        String hashed = "hashedPassword";

        when(passwordEncoder.encode(raw)).thenReturn(hashed);

        String result = authRepo.hashPassword(raw);

        assertEquals(hashed, result);
        verify(passwordEncoder).encode(raw);
    }

    @Test
    void shouldVerifyPassword() {
        String raw = "password123";
        String hashed = "hashedPassword";

        when(passwordEncoder.matches(raw, hashed)).thenReturn(true);

        boolean result = authRepo.verifyPassword(raw, hashed);

        assertTrue(result);
        verify(passwordEncoder).matches(raw, hashed);
    }

    @Test
    void shouldGenerateToken() {
        User user = new User();
        String token = "jwtToken";

        when(jwtProvider.generateToken(user)).thenReturn(Mono.just(token));

        StepVerifier.create(authRepo.generateToken(user))
                .expectNext(token)
                .verifyComplete();

        verify(jwtProvider).generateToken(user);
    }

    @Test
    void shouldValidateToken() {
        String token = "jwtToken";
        User user = new User();
        user.setEmail("test@example.com");

        when(jwtProvider.validate(token)).thenReturn(Mono.just(user));

        StepVerifier.create(authRepo.validateToken(token))
                .expectNextMatches(u -> u.getEmail().equals("test@example.com"))
                .verifyComplete();

        verify(jwtProvider).validate(token);
    }
}

