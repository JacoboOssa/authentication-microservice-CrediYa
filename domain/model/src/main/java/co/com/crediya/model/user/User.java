package co.com.crediya.model.user;

import co.com.crediya.model.rol.Rol;
import lombok.*;

import java.math.BigDecimal;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class User {
    private String id;
    private String name;
    private String lastName;
    private String birthDate;
    private String address;
    private String phoneNumber;
    private String email;
    private String password;
    private String identificationNumber;
    private BigDecimal baseSalary;
    private Rol role;
}
