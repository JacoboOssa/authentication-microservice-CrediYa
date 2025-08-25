package co.com.crediya.api.dto.response;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record RoleDTO(
        String id,
        String name,
        String description
) implements Serializable {
}
