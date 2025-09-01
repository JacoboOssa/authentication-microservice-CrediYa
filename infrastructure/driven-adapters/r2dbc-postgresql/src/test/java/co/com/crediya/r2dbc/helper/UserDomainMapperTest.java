package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.util.UserEntityUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDomainMapperTest {

    @Mock
    private RolRepository rolRepository;

    @Mock
    private RolEntityMapper rolEntityMapper;

    @Mock
    private UserEntityMapper userMapper;

    @InjectMocks
    private UserDomainMapper userDomainMapper;

    @Test
    void mustMapUserEntityToDomainWithRole() {
        // Arrange
        UserEntity entity = UserEntityUtil.userEntity();

        User userBasic = User.builder()
                .email(entity.getEmail())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .identificationNumber(entity.getIdentificationNumber())
                .build();

        Rol role = Rol.builder()
                .id(entity.getRolId())
                .name("Cliente")
                .build();

        when(userMapper.toDomain(entity)).thenReturn(userBasic);
        when(rolRepository.findById(entity.getRolId())).thenReturn(Mono.just(role));

        // Act & Assert
        StepVerifier.create(userDomainMapper.toDomain(entity))
                .expectNextMatches(user ->
                        user.getEmail().equals(entity.getEmail())
                                && user.getRole().getId().equals(entity.getRolId())
                                && user.getRole().getName().equals("Cliente")
                )
                .verifyComplete();
    }

    @Test
    void mustThrowExceptionWhenRoleNotFound() {
        // Arrange
        UserEntity entity = UserEntityUtil.userEntity();

        User userBasic = User.builder()
                .email(entity.getEmail())
                .build();

        when(userMapper.toDomain(entity)).thenReturn(userBasic);
        when(rolRepository.findById(entity.getRolId())).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(userDomainMapper.toDomain(entity))
                .expectErrorMatches(error ->
                        error instanceof BusinessException
                                && error.getMessage().equals(BusinessException.ROLE_ID_NOT_FOUND)
                )
                .verify();
    }
}
