package co.com.crediya.r2dbc;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.UserEntityMapper;
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
class UserReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    UserEntityMapper userEntityMapper;


    private final UserEntity userEntity = UserEntity.builder()
            .id("1")
            .name("Jacobo")
            .lastName("Ossa")
            .birthDate("1990-01-01")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .email("jaco@gmail.com")
            .identificationNumber("123456789")
            .baseSalary(50000.0)
            .rolId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")
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
        user.setEmail("jaco@gmail.com");
        user.setIdentificationNumber("123456789");
        user.setBaseSalary(50000.0);
        user.setRole(rol);
    }



    @Test
    void mustSaveNewUser() {
        when(userEntityMapper.toDomain(userEntity)).thenReturn(user);
        when(userEntityMapper.toEntity(user)).thenReturn(userEntity);
        when(repository.save(any(UserEntity.class))).thenReturn(Mono.just(userEntity));

        Mono<User> userMono = repositoryAdapter.save(user);

        StepVerifier.create(userMono)
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustExistByEmail() {
        String email = "jaco@gmail.com";
        when(repository.existsByEmail(email)).thenReturn(Mono.just(true));

        Mono<Boolean> userExist = repositoryAdapter.existByEmail(email);

        StepVerifier.create(userExist)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustNotExistByEmail() {
        String email = "jaco@gmail.com";
        when(repository.existsByEmail(email)).thenReturn(Mono.just(false));
        Mono<Boolean> userExist = repositoryAdapter.existByEmail(email);
        StepVerifier.create(userExist)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void mustExistByIdentificationNumber() {
        String identificationNumber = "123456789";
        when(repository.existsByIdentificationNumber(identificationNumber)).thenReturn(Mono.just(true));

        Mono<Boolean> userExist = repositoryAdapter.existByIdentificationNumber(identificationNumber);

        StepVerifier.create(userExist)
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void mustNotExistByIdentificationNumber() {
        String identificationNumber = "123456789";
        when(repository.existsByIdentificationNumber(identificationNumber)).thenReturn(Mono.just(false));
        Mono<Boolean> userExist = repositoryAdapter.existByIdentificationNumber(identificationNumber);
        StepVerifier.create(userExist)
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void mustPropagateErrorWhenExistByEmailFails() {
        when(repository.existsByEmail("jacotaco@gmail.com"))
                .thenReturn(Mono.error(new RuntimeException("Constraint violation")));

        StepVerifier.create(repositoryAdapter.existByEmail("jacotaco@gmail.com"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Constraint violation"))
                .verify();
    }

    @Test
    void mustPropagateErrorWhenExistByIdentificationNumberFails() {
        when(repository.existsByIdentificationNumber("987654321"))
                .thenReturn(Mono.error(new RuntimeException("Constraint violation")));

        StepVerifier.create(repositoryAdapter.existByIdentificationNumber("987654321"))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Constraint violation"))
                .verify();
    }

    @Test
    void mustRetrieveEmailByIdentificationNumber() {
        String identificationNumber = "123456789";

        when(repository.getEmailByIdentificationNumber(identificationNumber)).thenReturn(Mono.just("jaco@gmail.com"));

        StepVerifier.create(repositoryAdapter.getEmailByIdentificationNumber(identificationNumber))
                .expectNext("jaco@gmail.com")
                .verifyComplete();
    }

    @Test
    void mustPropagateErrorWhenGetEmailByIdentificationNumberFails() {
        String identificationNumber = "123456789";

        when(repository.getEmailByIdentificationNumber(identificationNumber))
                .thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(repositoryAdapter.getEmailByIdentificationNumber(identificationNumber))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Database error"))
                .verify();
    }



}
