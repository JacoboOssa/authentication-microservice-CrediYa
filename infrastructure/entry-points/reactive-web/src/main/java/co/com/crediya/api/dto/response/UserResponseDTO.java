package co.com.crediya.api.dto.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record UserResponseDTO(
        String id,
        String name,
        String lastName,
        String birthDate,
        String address,
        String phoneNumber,
        String email,
        String identificationNumber,
        Double baseSalary,
        RoleDTO role
) implements Serializable {
}
