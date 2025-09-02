package co.com.crediya.api.config;

import co.com.crediya.api.Handler;
import co.com.crediya.api.RouterRest;
import co.com.crediya.api.dto.response.RoleDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.util.LoginDTOUtil;
import co.com.crediya.api.validator.UserValidator;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import co.com.crediya.transaction.TransactionalAdapter;
import co.com.crediya.usecase.login.LogInUseCase;
import co.com.crediya.usecase.user.UserUseCase;
import co.com.crediya.usecase.validatetoken.ValidateTokenUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@TestPropertySource(properties = {"routes.paths.save-user=/api/v1/usuarios", "routes.paths.get-all-users=/api/v1/usuarios", "routes.paths.get-user-email-by-id-number=/api/v1/usuarios/{identificationNumber}", "routes.paths.log-in=/auth/api/v1/login", "routes.paths.validate=/auth/api/v1/validate"})
@EnableConfigurationProperties(UserPath.class)
@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class, TestSecurityConfig.class})
class ConfigTest {


    @MockitoBean
    private UserUseCase userUseCase;

    @MockitoBean
    private LogInUseCase logInUseCase;

    @MockitoBean
    private ValidateTokenUseCase validateTokenUseCase;

    @MockitoBean
    private UserDTOMapper userDTOMapper;

    @MockitoBean
    private UserValidator userValidator;

    @MockitoBean
    private TransactionalAdapter transactionalAdapter;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private UserPath userPath;


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
            .role(roleDTO)
            .build();


    @BeforeEach
    void setUp() {
        when(userUseCase.getAllUsers()).thenReturn(Flux.just(user));

        when(userDTOMapper.toDto(user)).thenReturn(userResponseDTO);

        when(logInUseCase.logIn("ferna@gmail.com", "Chacal1125*"))
                .thenReturn(Mono.just("fake-jwt-token"));

    }


    @Test
    void corsConfigurationShouldAllowOrigins() {
        String logInPath = "/auth/api/v1/login";
        webTestClient.post()
                .uri(logInPath)
                .bodyValue(LoginDTOUtil.logInDTO())
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin");
    }

}