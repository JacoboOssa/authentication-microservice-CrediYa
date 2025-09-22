package co.com.crediya.api.util;

import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.model.user.User;

import java.math.BigDecimal;

public class UserUtil {
    public static User user1(){
        return User.builder()
                .id("1")
                .name("Lionel")
                .lastName("Messi")
                .birthDate("1990-01-01")
                .address("calle")
                .phoneNumber("12312412")
                .email("mesi@gmail.com")
                .password("1234")
                .identificationNumber("819347912")
                .baseSalary(BigDecimal.valueOf(1200000.0))
                .role(RolUtil.rolAdmin())
                .build();
    }

    public static User user2(){
        return User.builder()
                .id("2")
                .name("Cristiano")
                .lastName("Ronaldo")
                .birthDate("1990-09-01")
                .address("calle")
                .phoneNumber("909090")
                .email("bichi@gmail.com")
                .password("1234")
                .identificationNumber("281039")
                .baseSalary(BigDecimal.valueOf(1200000.0))
                .role(RolUtil.rolClient())
                .build();
    }

    public static UserResponseDTO userResponseDTOUser1(){
        return UserResponseDTO.builder()
                .id("1")
                .name("Lionel")
                .lastName("Messi")
                .birthDate("1990-01-01")
                .address("calle")
                .phoneNumber("12312412")
                .email("mesi@gmail.com")
                .identificationNumber("819347912")
                .baseSalary(BigDecimal.valueOf(1200000.0))
                .role(RolUtil.roleAdminDTO())
                .build();
    }

    public static UserResponseDTO userResponseDTOUser2(){
        return UserResponseDTO.builder()
                .id("2")
                .name("Cristiano")
                .lastName("Ronaldo")
                .birthDate("1990-09-01")
                .address("calle")
                .phoneNumber("909090")
                .email("bichi@gmail.com")
                .identificationNumber("281039")
                .baseSalary(BigDecimal.valueOf(1200000.0))
                .role(RolUtil.roleClientDTO())
                .build();

    }


}
