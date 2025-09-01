package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.r2dbc.entity.RolEntity;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class RolEntityMapperTest {

    private final RolEntityMapper mapper = Mappers.getMapper(RolEntityMapper.class);

    @Test
    void mustMapRolToEntity() {
        Rol rol = new Rol();
        rol.setId("123");
        rol.setName("Admin");
        rol.setDescription("Administrator role");

        RolEntity entity = mapper.toEntity(rol);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo("123");
        assertThat(entity.getName()).isEqualTo("Admin");
        assertThat(entity.getDescription()).isEqualTo("Administrator role");
    }

    @Test
    void mustMapEntityToRol() {
        RolEntity entity = RolEntity.builder()
                .id("456")
                .name("User")
                .description("Standard user")
                .build();

        Rol rol = mapper.toDomain(entity);

        assertThat(rol).isNotNull();
        assertThat(rol.getId()).isEqualTo("456");
        assertThat(rol.getName()).isEqualTo("User");
        assertThat(rol.getDescription()).isEqualTo("Standard user");
    }

    @Test
    void mustHandleNullGracefully() {
        assertThat(mapper.toEntity(null)).isNull();
        assertThat(mapper.toDomain(null)).isNull();
    }
}
