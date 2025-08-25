package co.com.crediya.r2dbc.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @Column("id")
    private String id;
    @Column("name")
    private String name;
    @Column("last_name")
    private String lastName;
    @Column("birth_date")
    private String birthDate;
    @Column("address")
    private String address;
    @Column("phone_number")
    private String phoneNumber;
    @Column("email")
    private String email;
    @Column("identification_number")
    private String identificationNumber;
    @Column("base_salary")
    private Double baseSalary;
    @Column("rol_id")
    private String rolId;
}
