package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.model.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserDTOMapper {

    @Mapping(source = "roleId", target = "role.id")
    User toDomain(CreateUserRequestDTO createUserRequestDTO);

    UserResponseDTO toDto(User user);


}
