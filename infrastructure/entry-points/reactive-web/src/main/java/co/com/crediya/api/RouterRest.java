package co.com.crediya.api;

import co.com.crediya.api.config.UserPath;
import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.request.LogInDTO;
import co.com.crediya.api.dto.response.ErrorResponseDTO;
import co.com.crediya.api.dto.response.TokenDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.api.dto.response.ValidatedTokenDTO;
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
                    beanMethod = "saveUser",
                    operation = @Operation(
                            summary = "Create User",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = CreateUserRequestDTO.class))
                            ),
                            operationId = "saveUser",
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
                    beanMethod = "getAllUsers",
                    operation = @Operation(
                            summary = "Get All Users",
                            operationId = "getAllUsers",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                            )
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/email/{identificationNumber}",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getEmailByIdentificationNumber",
                    operation = @Operation(
                            summary = "Get User Email by Identification Number",
                            operationId = "getEmailByIdentificationNumber",
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = String.class))
                            )
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/login",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "logIn",
                    operation = @Operation(
                            summary = "User Log In",
                            operationId = "logIn",
                            requestBody = @RequestBody(
                                    content = @Content(schema = @Schema(implementation = LogInDTO.class))
                            ),
                            responses = @ApiResponse(
                                    responseCode = "200",
                                    description = "Successful operation",
                                    content = @Content(schema = @Schema(implementation = TokenDTO.class))
                            )
                    )
            ),
            @RouterOperation(
                    path = "/auth/api/v1/validate",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "validateToken",
                    operation = @Operation(
                            summary = "Validate token (JWT)",
                            operationId = "validateToken",
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Token is valid",
                                            content = @Content(schema = @Schema(implementation = ValidatedTokenDTO.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Authorization header is empty or not bearer token",
                                            content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
                                    )
                            }
                    )
            )

    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(userPath.getSaveUser()), handler::saveUser)
                .andRoute(GET(userPath.getGetAllUsers()), handler::getAllUsers)
                .andRoute(GET(userPath.getGetUserEmailByIdNumber()), handler::getEmailByIdentificationNumber)
                .andRoute(POST(userPath.getLogIn()), handler::logIn)
                .andRoute(GET(userPath.getValidate()),handler::validateToken);
    }
}
