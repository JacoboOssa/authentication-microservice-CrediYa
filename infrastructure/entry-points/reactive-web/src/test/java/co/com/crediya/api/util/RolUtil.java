package co.com.crediya.api.util;

import co.com.crediya.api.dto.response.RoleDTO;
import co.com.crediya.model.rol.Rol;

public class RolUtil {
    public static Rol rolAdmin(){
        return Rol.builder()
                .id("1")
                .name("ROLE_ADMIN")
                .description("Rol de admin")
                .build();
    }

    public static Rol rolClient(){
        return Rol.builder()
                .id("2")
                .name("ROLE_CLIENT")
                .description("Rol de Cliente")
                .build();
    }

    public static RoleDTO roleAdminDTO(){
        return RoleDTO.builder()
                .id("1")
                .name("ROLE_ADMIN")
                .description("Rol de admin")
                .build();
    }

    public static RoleDTO roleClientDTO(){
        return RoleDTO.builder()
                .id("2")
                .name("ROLE_CLIENT")
                .description("Rol de Cliente")
                .build();
    }


}
