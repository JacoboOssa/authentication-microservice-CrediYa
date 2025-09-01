package co.com.crediya.usecase.util;

import co.com.crediya.model.rol.Rol;

public class RolUtil {
    public static Rol adminRol(){
        return Rol.builder()
                .id("1")
                .name("ROLE_ADMIN")
                .description("Administrator role with full access")
                .build();

    }

    public static Rol clientRol(){
        return Rol.builder()
                .id("2")
                .name("ROLE_CLIENT")
                .description("Client role with limited access")
                .build();

    }
}
