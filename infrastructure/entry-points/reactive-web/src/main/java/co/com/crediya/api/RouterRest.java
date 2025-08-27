package co.com.crediya.api;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    private final UserPath userPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveUser",
                    operation = @Operation(
                            summary = "Create User",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateUserRequestDTO.class))
                            ),
                            operationId = "listenSaveUser",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                            )
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGetAllUsers",
                    operation = @Operation(
                            summary = "Get All Users",
                            operationId = "listenGetAllUsers",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                            )
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(userPath.getSaveUser()), handler::listenSaveUser)
                .andRoute(GET(userPath.getGetAllUsers()), handler::listenGetAllUsers)
                .andRoute(GET(userPath.getGetUserEmailByIdNumber()), handler::listenGetEmailByIdentificationNumber);
    }
}
