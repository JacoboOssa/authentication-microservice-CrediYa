package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.rol.Rol;
import co.com.crediya.r2dbc.entity.RolEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface RolEntityMapper {
    RolEntity toEntity(Rol rol);

    Rol toDomain(RolEntity entity);
}
