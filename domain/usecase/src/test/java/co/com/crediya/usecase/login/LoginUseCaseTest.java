package co.com.crediya.usecase.login;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.usecase.util.UserUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoginUseCaseTest {

    @Mock
    UserRepository userRepository;

    @Mock
    AuthRepository authRepository;

    @InjectMocks
    LogInUseCase logInUseCase;

    @Test
    void mustValidateCredentialsAndReturnUser() {
        when(authRepository.verifyPassword("securePassword",UserUtil.user().getPassword())).thenReturn(true);

        StepVerifier.create(logInUseCase.validateCredentials(UserUtil.user(), "securePassword"))
                .expectNextMatches(user -> user.getEmail().equals("ac@gmail.com"))
                .verifyComplete();
    }

    @Test
    void mustValidateCredentialsAndPropagateBadCredentialsError(){
        when(authRepository.verifyPassword("wrongPassword",UserUtil.user().getPassword())).thenReturn(false);

        StepVerifier.create(logInUseCase.validateCredentials(UserUtil.user(), "wrongPassword"))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(BusinessException.BAD_CREDENTIALS))
                .verify();
    }

    @Test
    void mustLoginUserAndReturnToken(){
        User loginUser = UserUtil.loginUser();

        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Mono.just(loginUser));
        when(authRepository.verifyPassword("securePassword", loginUser.getPassword())).thenReturn(true);
        when(authRepository.generateToken(loginUser)).thenReturn(Mono.just("validToken"));

        StepVerifier.create(logInUseCase.logIn(loginUser.getEmail(), loginUser.getPassword()))
                .expectNextMatches(token -> token.equals("validToken"))
                .verifyComplete();
    }

    @Test
    void mustReturnErrorWhenUserNotFoundDuringLogin(){
        User loginUser = UserUtil.loginUser();
        when(userRepository.findByEmail(loginUser.getEmail())).thenReturn(Mono.empty());

        StepVerifier.create(logInUseCase.logIn(loginUser.getEmail(), loginUser.getPassword()))
                .expectErrorMatches(throwable -> throwable instanceof BusinessException &&
                        throwable.getMessage().equals(BusinessException.USER_NOT_FOUND))
                .verify();

    }


}
