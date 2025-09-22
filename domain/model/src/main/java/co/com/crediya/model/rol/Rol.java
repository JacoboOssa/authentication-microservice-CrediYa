package co.com.crediya.model.rol;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rol {
    private String id;
    private String name;
    private String description;
}
