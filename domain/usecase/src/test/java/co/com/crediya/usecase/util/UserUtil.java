package co.com.crediya.usecase.util;

import co.com.crediya.model.user.User;

public class UserUtil {
    public static User user(){
        return User.builder()
                .id("1")
                .name("Jacobo")
                .lastName("Perez")
                .birthDate("1990-05-15")
                .address("123 Main St, Cityville")
                .phoneNumber("555-1234")
                .email("ac@gmail.com")
                .password("securePassword")
                .identificationNumber("ID123456")
                .baseSalary(50000.0)
                .role(RolUtil.clientRol())
                .build();
    }

    public static User loginUser(){
        return User.builder()
                .id("2")
                .name("Juan")
                .lastName("Madrid")
                .birthDate("1995-12-15")
                .address("423 Main St, Cityville")
                .phoneNumber("555-1234")
                .email("madrid@gmail.com")
                .password("securePassword")
                .identificationNumber("110823")
                .baseSalary(45000.0)
                .role(RolUtil.adminRol())
                .build();
    }
}
