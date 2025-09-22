package co.com.crediya.usecase.login;


import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;


@AllArgsConstructor
public class LogInUseCase {
    private final UserRepository userRepository;
    private final AuthRepository authRepository;


    public Mono<String> logIn(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.USER_NOT_FOUND)))
                .flatMap(user -> validateCredentials(user, password))
                .flatMap(authRepository::generateToken);
    }

    public Mono<User> validateCredentials(User user, String password) {
        if (!authRepository.verifyPassword(password, user.getPassword())) {
            return Mono.error(new BusinessException(BusinessException.BAD_CREDENTIALS));
        }
        return Mono.just(user);
    }



}
