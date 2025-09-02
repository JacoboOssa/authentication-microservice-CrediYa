package co.com.crediya.usecase.validatetoken;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class ValidateTokenUseCase {
    private final AuthRepository authRepository;

    public Mono<User> validateToken(String token) {
        return authRepository.validateToken(token);
    }
}
