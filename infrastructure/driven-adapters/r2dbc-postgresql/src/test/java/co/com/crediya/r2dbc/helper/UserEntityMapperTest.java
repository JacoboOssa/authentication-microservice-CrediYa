package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;


public class UserEntityMapperTest {
    private final UserEntityMapper userEntityMapper = Mappers.getMapper(UserEntityMapper.class);

    @Test
    void mustMapUserToEntity() {

        Rol role = new Rol();
        role.setId("1");
        role.setName("Admin");
        role.setDescription("Administrator role");

        User user = new User();
        user.setId("123");
        user.setName("John ");
        user.setEmail("john@gmail.com");
        user.setLastName("Doe");
        user.setAddress("123 Main St");
        user.setBirthDate("1990-01-01");
        user.setPhoneNumber("555-1234");
        user.setIdentificationNumber("ID123456");
        user.setBaseSalary(BigDecimal.valueOf(50000.0));
        user.setRole(role);

        UserEntity userEntity = userEntityMapper.toEntity(user);

        assertThat(userEntity).isNotNull();
        assertThat(userEntity.getId()).isEqualTo("123");
        assertThat(userEntity.getName()).isEqualTo("John ");
    }

    @Test
    void mustMapEntityToUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId("123");
        userEntity.setName("John ");
        userEntity.setEmail("john@gmail.com");
        userEntity.setLastName("Doe");
        userEntity.setAddress("123 Main St");
        userEntity.setBirthDate("1990-01-01");
        userEntity.setPhoneNumber("555-1234");
        userEntity.setIdentificationNumber(("ID123456"));
        userEntity.setBaseSalary(BigDecimal.valueOf(50000.0));
        userEntity.setRolId("1");

        User user = userEntityMapper.toDomain(userEntity);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo("123");
        assertThat(user.getName()).isEqualTo("John ");
    }
}
