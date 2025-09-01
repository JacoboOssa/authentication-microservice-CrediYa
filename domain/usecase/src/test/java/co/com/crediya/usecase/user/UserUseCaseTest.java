package co.com.crediya.usecase.user;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class UserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private AuthRepository authRepository;

    @InjectMocks
    private UserUseCase userUseCase;

    private User user;
    private User user2;
    private Rol rol1;
    private Rol rol2;
    private User user3;

    @BeforeEach
    void setUp() {

        rol1 = new Rol("role1", "Admin", "Administrator role");
        rol2 = new Rol("role2", "User", "Standard user role");

        user = new User();
        user.setId("1");
        user.setName("Test User");
        user.setLastName("Test");
        user.setBirthDate("1990-01-01");
        user.setAddress("123 Test St");
        user.setPhoneNumber("4324");
        user.setEmail("jon@gmail.com");
        user.setIdentificationNumber("123456789");
        user.setBaseSalary(50000.0);
        user.setRole(rol1);

        user2 = new User();
        user2.setId("2");
        user2.setName("Another User");
        user2.setLastName("User");
        user2.setBirthDate("1985-05-15");
        user2.setAddress("456 Another St");
        user2.setPhoneNumber("5678");
        user2.setEmail("as@gmail.com");
        user2.setIdentificationNumber("987654321");
        user2.setBaseSalary(60000.0);
        user2.setRole(rol2);

        user3 = new User();
        user3.setId("3");
        user3.setName("Third User");
        user3.setLastName("Third");
        user3.setBirthDate("1975-07-20");
        user3.setAddress("789 Third St");
        user3.setPhoneNumber("9101");
        user3.setEmail("jaco@gmail.com");
        user3.setIdentificationNumber("1107836154");
        user3.setBaseSalary(70000.0);
        user3.setRole(rol1);


    }

    @Test
    void mustCreateUserSucessfully() {
        when(userRepository.existByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(false));
        when(rolRepository.findById(user.getRole().getId())).thenReturn(Mono.just(user.getRole()));
        when(authRepository.hashPassword(user.getPassword())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));


        StepVerifier.create(userUseCase.createUser(user))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    void mustFailWhenEmailExists() {
        when(userRepository.existByEmail(user.getEmail())).thenReturn(Mono.just(true));
        when(userRepository.existByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(false));
        when(rolRepository.findById(user.getRole().getId())).thenReturn(Mono.just(user.getRole()));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains(BusinessException.EMAIL_ALREADY_IN_USE + user.getEmail()))
                .verify();
    }

    @Test
    void mustFailWhenIdentificationExists() {
        when(userRepository.existByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(true));
        when(rolRepository.findById(user.getRole().getId())).thenReturn(Mono.just(user.getRole()));

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains(BusinessException.IDENTIFICATION_NUMBER_ALREADY_IN_USE + user.getIdentificationNumber()))
                .verify();
    }

    @Test
    void mustFailWhenRoleNotFound() {
        when(userRepository.existByEmail(user.getEmail())).thenReturn(Mono.just(false));
        when(userRepository.existByIdentificationNumber(user.getIdentificationNumber())).thenReturn(Mono.just(false));
        when(rolRepository.findById(user.getRole().getId())).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.createUser(user))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains(BusinessException.ROLE_ID_NOT_FOUND + user.getRole().getId()))
                .verify();
    }

    @Test
    void mustRetrieveAllUsers() {
        when(userRepository.findAll()).thenReturn(Flux.just(user, user2));
        when(rolRepository.findById("role1")).thenReturn(Mono.just(rol1));
        when(rolRepository.findById("role2")).thenReturn(Mono.just(rol2));


        StepVerifier.create(userUseCase.getAllUsers())
                .expectNextMatches(u ->
                        u.getEmail().equals(user.getEmail()) &&
                                u.getRole().equals(rol1)
                )
                .expectNextMatches(u ->
                        u.getEmail().equals(user2.getEmail()) &&
                                u.getRole().equals(rol2)
                )
                .verifyComplete();
    }

    @Test
    void mustRetriveEmailByIdentificationNumber() {
        when(userRepository.getEmailByIdentificationNumber("1107836154")).thenReturn(Mono.just(user3.getEmail()));

        StepVerifier.create(userUseCase.getEmailByIdentificationNumber("1107836154"))
                .expectNext(user3.getEmail())
                .verifyComplete();
    }

    @Test
    void mustFailWhenIdentificationNumberNotFound() {
        when(userRepository.getEmailByIdentificationNumber("0000000000")).thenReturn(Mono.empty());

        StepVerifier.create(userUseCase.getEmailByIdentificationNumber("0000000000"))
                .expectErrorMatches(throwable ->
                        throwable instanceof BusinessException &&
                                throwable.getMessage().contains(BusinessException.USER_WITH_IDENTIFICATION_NOT_FOUND + "0000000000"))
                .verify();
    }


}

