package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class UserDTOMapperTest {

    private final UserDTOMapper userDTOMapper = Mappers.getMapper(UserDTOMapper.class);

    private CreateUserRequestDTO createUserRequestDTO = CreateUserRequestDTO.builder()
            .name("John")
            .lastName("Doe")
            .birthDate("1990-01-01")
            .address("123 Main St")
            .phoneNumber("555-1234")
            .identificationNumber("ID123456")
            .email("jon@gmail.com")
            .baseSalary(BigDecimal.valueOf(5000000.0))
            .roleId("role-uuid-1234")
            .build();

    @Test
    void mustMapToDomain() {
        Rol rol = new Rol();
        rol.setId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");
        rol.setName("Admin");
        rol.setDescription("Administrator role");

        User user = new User();
        user.setName("John");
        user.setLastName("Doe");
        user.setBirthDate("1990-01-01");
        user.setAddress("123 Main St");
        user.setPhoneNumber("555-1234");
        user.setIdentificationNumber("ID123456");
        user.setEmail("jon@gmail.com");
        user.setBaseSalary(BigDecimal.valueOf(000000.0));
        user.setRole(rol);

        User mappedUser = userDTOMapper.toDomain(createUserRequestDTO);

        assertThat(mappedUser).isNotNull();
        assertThat(mappedUser.getName()).isEqualTo(user.getName());
        assertThat(mappedUser.getLastName()).isEqualTo(user.getLastName());
        assertThat(mappedUser.getBirthDate()).isEqualTo(user.getBirthDate());
    }

    @Test
    void mustMapToDto() {
        Rol rol = new Rol();
        rol.setId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");
        rol.setName("Admin");
        rol.setDescription("Administrator role");

        User user = new User();
        user.setId("user-uuid-1234");
        user.setName("John");
        user.setLastName("Doe");
        user.setBirthDate("1990-01-01");
        user.setAddress("123 Main St");
        user.setPhoneNumber("555-1234");
        user.setIdentificationNumber("ID123456");
        user.setEmail("jon@gmail.com");
        user.setBaseSalary(BigDecimal.valueOf(000000.0));
        user.setRole(rol);

        UserResponseDTO userResponseDTO = userDTOMapper.toDto(user);

        assertThat(userResponseDTO).isNotNull();
        assertThat(userResponseDTO.id()).isEqualTo(user.getId());
        assertThat(userResponseDTO.name()).isEqualTo(user.getName());
    }
}
