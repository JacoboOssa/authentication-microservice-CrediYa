package co.com.crediya.api.validator;

import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidatorTest {
    @Mock
    Validator validator;

    @InjectMocks
    UserValidator userValidator;

    private final CreateUserRequestDTO userRequestDTO1 = CreateUserRequestDTO.builder()
            .name("John")
            .lastName("Doe")
            .birthDate("1990-01-01")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .identificationNumber("ID123456")
            .email("jon@gmail.com")
            .baseSalary(5000000.0)
            .roleId("role-uuid-1234")
            .build();

    @Test
    void mustValidateSuccessfully() {
        when(validator.validate(userRequestDTO1)).thenReturn(Collections.emptySet());

        StepVerifier.create(userValidator.validate(userRequestDTO1))
                .expectNext(userRequestDTO1)
                .verifyComplete();
    }

}
