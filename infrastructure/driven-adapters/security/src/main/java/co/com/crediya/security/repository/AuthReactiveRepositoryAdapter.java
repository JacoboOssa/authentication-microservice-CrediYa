package co.com.crediya.security.repository;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.UserEntityMapper;
import co.com.crediya.security.jwt.provider.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@AllArgsConstructor
public class AuthReactiveRepositoryAdapter implements AuthRepository {

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserEntityMapper userEntityMapper;

    @Override
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }

    @Override
    public Mono<String> generateToken(User user) {
        return jwtProvider.generateToken(user);
    }
}
