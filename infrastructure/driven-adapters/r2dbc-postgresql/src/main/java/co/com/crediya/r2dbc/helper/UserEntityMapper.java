package co.com.crediya.r2dbc.helper;

import co.com.crediya.model.user.User;
import co.com.crediya.r2dbc.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserEntityMapper {
    @Mapping(source = "role.id", target = "rolId")
    UserEntity toEntity(User user);

    @Mapping(source = "rolId", target = "role.id")
    User toDomain(UserEntity userEntity);
}
