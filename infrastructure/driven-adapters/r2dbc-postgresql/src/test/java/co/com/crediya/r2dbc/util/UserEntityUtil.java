package co.com.crediya.r2dbc.util;

import co.com.crediya.r2dbc.entity.UserEntity;

public class UserEntityUtil {
    public static UserEntity userEntity(){
        return UserEntity.builder()
                .id("1")
                .name("John")
                .lastName("Doe")
                .birthDate("1990-01-01")
                .address("123 Main St")
                .phoneNumber("1234567890")
                .email("jon@gmail.com")
                .password("password")
                .identificationNumber("123456789")
                .baseSalary(3000000.0)
                .rolId("2")
                .build();
    }
}
