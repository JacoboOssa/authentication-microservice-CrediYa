package co.com.crediya.r2dbc;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.rol.gateways.RolRepository;

import co.com.crediya.r2dbc.entity.RolEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbc.helper.RolEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RolReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Rol,
        RolEntity,
        String,
        RolReactiveRepository
        > implements RolRepository {
    private final RolEntityMapper rolEntityMapper;
    protected RolReactiveRepositoryAdapter(RolReactiveRepository repository, ObjectMapper mapper, RolEntityMapper rolEntityMapper) {
        super(repository, mapper, rolEntityMapper::toDomain);
        this.rolEntityMapper = rolEntityMapper;
    }

    @Override
    protected RolEntity toData(Rol rol) {
        return rolEntityMapper.toEntity(rol);
    }

}
