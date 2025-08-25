package co.com.crediya.r2dbc.entity;

import org.springframework.data.relational.core.mapping.Column;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "rol")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolEntity {
    @Id
    @Column("id")
    private String id;
    @Column("name")
    private String name;
    @Column("description")
    private String description;
}
