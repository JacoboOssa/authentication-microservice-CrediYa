package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@AllArgsConstructor
public class UserDomainMapper {
    private final RolRepository rolRepository;
    private final RolEntityMapper rolMapper;
    private final UserEntityMapper userMapper;

    public Mono<User> toDomain(UserEntity entity) {
        User userBasic = userMapper.toDomain(entity);

        return Mono.justOrEmpty(entity.getRolId())
                .flatMap(rolId -> rolRepository.findById(rolId)
                        .switchIfEmpty(Mono.error(new BusinessException(BusinessException.ROLE_ID_NOT_FOUND))))
                .map(role -> buildUserWithRole(userBasic, role))
                .doOnNext(user -> log.info("Mapped user {} with role {}", user.getEmail(), user.getRole().getName()))
                .doOnError(error -> log.error("Error mapping user {}: {}", userBasic.getEmail(), error.getMessage()));
    }

    private User buildUserWithRole(User userBasic, Rol role) {
        return userBasic.toBuilder()
                .role(role)
                .build();
    }
}
