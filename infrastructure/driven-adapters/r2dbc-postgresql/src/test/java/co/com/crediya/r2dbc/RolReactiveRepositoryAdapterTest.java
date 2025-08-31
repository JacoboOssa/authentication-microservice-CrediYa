package co.com.crediya.r2dbc;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.r2dbc.entity.RolEntity;
import co.com.crediya.r2dbc.helper.RolEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RolReactiveRepositoryAdapterTest {

    @InjectMocks
    RolReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    RolReactiveRepository repository;

    @Mock
    RolEntityMapper rolEntityMapper;

    @Mock
    ObjectMapper mapper;

    private final RolEntity rolEntity = RolEntity.builder()
            .id("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")
            .name("Admin")
            .description("Administrator role")
            .build();

    private final Rol rol = new Rol();
    {
        rol.setId("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");
        rol.setName("Admin");
        rol.setDescription("Administrator role");
    }



    @Test
    void mustGetById(){
        when(rolEntityMapper.toDomain(rolEntity)).thenReturn(rol);
        when(repository.findById("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")).thenReturn(Mono.just(rolEntity));

        Mono<Rol> rolMono = repositoryAdapter.findById("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");

        StepVerifier.create(rolMono)
                .expectNextMatches(t -> t.getId().equals(rol.getId()) && t.getName().equals(rol.getName()))
                .verifyComplete();
    }

    @Test
    void mustNotGetByIdWhenNotExists(){
        when(repository.findById("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037")).thenReturn(Mono.empty());

        Mono<Rol> rolMono = repositoryAdapter.findById("90f41a3f-3ae3-4b0b-8096-9b73cb6b2037");

        StepVerifier.create(rolMono)
                .expectNextCount(0)
                .verifyComplete();
    }
}
