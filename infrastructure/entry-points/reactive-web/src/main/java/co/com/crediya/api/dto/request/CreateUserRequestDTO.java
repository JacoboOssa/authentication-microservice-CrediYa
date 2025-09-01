package co.com.crediya.api.dto.request;


import jakarta.validation.constraints.*;
import lombok.Builder;
import org.hibernate.validator.constraints.UUID;


@Builder
public record CreateUserRequestDTO(
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Last name is required")
        String lastName,
        @NotBlank(message = "Birth date is required")
        String birthDate,
        @NotBlank(message = "Address is required")
        String address,
        @NotBlank(message = "Phone number is required")
        String phoneNumber,
        @NotBlank(message = "Identification number is required")
        String identificationNumber,
        @Email(message = "Email should be valid")
        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = "Password is required")
        String password,
        @NotNull(message = "Base salary is required")
        @Min(value = 0, message = "Base salary must be positive")
        @Max(value = 15000000, message = "Base salary must be less than or equal to 15,000,000")
        Double baseSalary,
        @UUID(message = "Role ID must be a valid UUID")
        @NotBlank(message = "Role ID is required")
        String roleId
) {}