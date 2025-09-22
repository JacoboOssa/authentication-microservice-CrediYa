package co.com.crediya.api.util;

import co.com.crediya.api.dto.request.LogInDTO;

public class LoginDTOUtil {
    public static LogInDTO logInDTO(){
        return LogInDTO.builder()
                .email("ferna@gmail.com")
                .password("Chacal1125*")
                .build();
    }
}
