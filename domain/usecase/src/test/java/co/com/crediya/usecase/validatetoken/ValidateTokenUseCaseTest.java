package co.com.crediya.usecase.validatetoken;

import co.com.crediya.model.exceptions.JwtException;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.usecase.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValidateTokenUseCaseTest {

    @Mock
    AuthRepository authRepository;

    @InjectMocks
    ValidateTokenUseCase validateTokenUseCase;

    @Test
    void mustRetrieveUserInfoWithValidToken() {
        when(authRepository.validateToken(any(String.class))).thenReturn(Mono.just(UserUtil.user()));

        StepVerifier.create(validateTokenUseCase.validateToken("validToken"))
                .expectNextMatches(user -> user.getEmail().equals("ac@gmail.com"))
                .verifyComplete();
    }

    @Test
    void mustFailWithInvalidToken() {
        when(authRepository.validateToken(any(String.class))).thenReturn(Mono.empty());

        StepVerifier.create(validateTokenUseCase.validateToken("invalidToken"))
                .expectErrorMatches(throwable -> throwable instanceof JwtException && throwable.getMessage().equals(JwtException.NO_USER_FOUND))
                .verify();
    }


}
