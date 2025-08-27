package co.com.crediya.api;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.response.RoleDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.api.exceptionhandler.GlobalErrorAttributes;
import co.com.crediya.api.exceptionhandler.GlobalExceptionHandler;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.validator.UserValidator;
import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import co.com.crediya.transaction.TransactionalAdapter;
import co.com.crediya.usecase.user.UserUseCase;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@EnableConfigurationProperties(UserPath.class)
@TestPropertySource(properties = {"routes.paths.save-user=/api/v1/usuarios", "routes.paths.get-all-users=/api/v1/usuarios"})
@WebFluxTest
@Import({GlobalExceptionHandler.class, GlobalErrorAttributes.class})
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private UserValidator userValidator;

    @MockitoBean
    private TransactionalAdapter transactionalAdapter;

    @Autowired
    private UserPath userPath;

    private final RoleDTO roleDTO = RoleDTO.builder()
            .id("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")
            .name("Admin")
            .description("Administrator role")
            .build();

    private final UserResponseDTO userResponseDTO = UserResponseDTO.builder()
            .id("1")
            .name("Jacobo")
            .lastName("Ossa")
            .email("fern@gmail.com")
            .identificationNumber("123456789")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .birthDate("1990-01-01")
            .baseSalary(5000000.0)
            .role(roleDTO).build();


    private final String saveUserPath = "/api/v1/usuarios";

    private final CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder()
            .name("Jacobo")
            .lastName("Ossa")
            .email("fern@gmail.com")
            .identificationNumber("123456789")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .birthDate("1990-01-01")
            .baseSalary(5000000.0)
            .roleId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")
            .build();

    private final CreateUserRequestDTO emptyCreateUserRequestDTO = CreateUserRequestDTO.builder()
            .name("")
            .lastName("Ossa")
            .email("fern@gmail.com")
            .identificationNumber("123456789")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .birthDate("1990-01-01")
            .baseSalary(5000000.0)
            .roleId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")
            .build();

    private final Rol rol = new Rol();
    {
        rol.setId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");
        rol.setName("Admin");
        rol.setDescription("Administrator role");
    }

    private final User user = new User();
    {
        user.setId("1");
        user.setName("Jacobo");
        user.setLastName("Ossa");
        user.setBirthDate("1990-01-01");
        user.setAddress("123 Main St");
        user.setPhoneNumber("555-1234");
        user.setEmail("fern@gmail.com");
        user.setIdentificationNumber("123456789");
        user.setBaseSalary(50000.0);
        user.setRole(rol);
    }


    @Test
    void shouldLoadTaskPathProperties() {
        assertEquals(saveUserPath, userPath.getSaveUser());
    }


    @Test
    void testListenSaveUser() {

        when(userValidator.validate(createUserRequestDTO)).thenReturn(Mono.just(createUserRequestDTO));

        when(transactionalAdapter.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.<Mono<?>>getArgument(0));

        when(userDTOMapper.toDomain(createUserRequestDTO)).thenReturn(user);

        when(userUseCase.createUser(user)).thenReturn(Mono.just(user));

        when(userDTOMapper.toDto(user)).thenReturn(userResponseDTO);

        webTestClient.post()
                .uri(saveUserPath)
                .bodyValue(createUserRequestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserResponseDTO.class)
                .value(response -> {
                    Assertions.assertThat(response).isNotNull();
                    Assertions.assertThat(response.id()).isEqualTo("1");
                    Assertions.assertThat(response.name()).isEqualTo("Jacobo");
                    Assertions.assertThat(response.lastName()).isEqualTo("Ossa");
                });
    }

    @Test
    void mustFailWhenNameIsEmpty() {
        when(transactionalAdapter.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.<Mono<?>>getArgument(0));

        when(userValidator.validate(any(CreateUserRequestDTO.class)))
                .thenReturn(Mono.error(new ConstraintViolationException(Set.of())));

        webTestClient.post()
                .uri(saveUserPath)
                .bodyValue(emptyCreateUserRequestDTO)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Validation Error")
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.path").isEqualTo("/api/v1/usuarios");
    }

    @Test
    void mustFailWhenEmailAlreadyExists() {
        when(userValidator.validate(createUserRequestDTO)).thenReturn(Mono.just(createUserRequestDTO));

        when(transactionalAdapter.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.<Mono<?>>getArgument(0));

        when(userDTOMapper.toDomain(createUserRequestDTO)).thenReturn(user);

        when(userUseCase.createUser(user))
                .thenReturn(Mono.error(new BusinessException("This email is already in use: " + user.getEmail())));

        webTestClient.post()
                .uri(saveUserPath)
                .bodyValue(createUserRequestDTO)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Business Rule Violation")
                .jsonPath("$.status").isEqualTo(409)
                .jsonPath("$.path").isEqualTo("/api/v1/usuarios");
    }

    @Test
    void mustFailWhenIdentificationNumberAlreadyExists() {
        when(userValidator.validate(createUserRequestDTO)).thenReturn(Mono.just(createUserRequestDTO));

        when(transactionalAdapter.executeInTransaction(any(Mono.class)))
                .thenAnswer(invocation -> invocation.<Mono<?>>getArgument(0));

        when(userDTOMapper.toDomain(createUserRequestDTO)).thenReturn(user);

        when(userUseCase.createUser(user))
                .thenReturn(Mono.error(new BusinessException("This identification number is already in use: " + user.getIdentificationNumber())));

        webTestClient.post()
                .uri(saveUserPath)
                .bodyValue(createUserRequestDTO)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.error").isEqualTo("Business Rule Violation")
                .jsonPath("$.status").isEqualTo(409)
                .jsonPath("$.path").isEqualTo("/api/v1/usuarios");
    }



}
