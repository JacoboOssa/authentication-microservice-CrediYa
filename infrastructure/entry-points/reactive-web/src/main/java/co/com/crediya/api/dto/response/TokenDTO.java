package co.com.crediya.api.dto.response;

import lombok.Builder;

@Builder
public record TokenDTO(
        String token
) {
}
