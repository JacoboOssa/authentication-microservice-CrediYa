package co.com.crediya.api;

import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.dto.request.LogInDTO;
import co.com.crediya.api.dto.response.UserResponseDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.validator.UserValidator;
import co.com.crediya.transaction.TransactionalAdapter;
import co.com.crediya.usecase.login.LogInUseCase;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final LogInUseCase logInUseCase;
    private final UserDTOMapper userDTOMapper;
    private final UserValidator userValidator;
    private final TransactionalAdapter transactionalAdapter;



    @PreAuthorize("hasAnyRole('ASESOR', 'ADMIN')")
    public Mono<ServerResponse> saveUser(ServerRequest serverRequest) {
        log.info("Received request to create user");
        return transactionalAdapter.executeInTransaction(
                serverRequest.bodyToMono(CreateUserRequestDTO.class)
                        .flatMap(userValidator::validate)
                        .map(userDTOMapper::toDomain)
                        .flatMap(userUseCase::createUser)
                        .map(userDTOMapper::toDto)
                        .doOnSuccess(dto-> log.info("User created successfully with id: {}", dto.id()))
                        .doOnError(error -> log.error("Error creating user: {}", error.getMessage()))
                        .flatMap(dto -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(dto))
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> getAllUsers(ServerRequest serverRequest) {
        log.info("Received request to get all users");
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userUseCase.getAllUsers().map(userDTOMapper::toDto), UserResponseDTO.class)
                .doOnSuccess(users -> log.info("Successfully retrieved all users"))
                .doOnError(error -> log.error("Error retrieving users: {}", error.getMessage()));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ServerResponse> getEmailByIdentificationNumber(ServerRequest serverRequest) {
        String identificationNumber = serverRequest.pathVariable("identificationNumber");
        log.info("Received request to get email by identification number: {}", identificationNumber);

        return userUseCase.getEmailByIdentificationNumber(identificationNumber)
                .flatMap(email -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("email", email)))
                .doOnSuccess(response -> log.info("Successfully retrieved email for identification number: {}", identificationNumber))
                .doOnError(error -> log.error("Error retrieving email for identification number {}: {}", identificationNumber, error.getMessage()));
    }


    public Mono<ServerResponse> logIn(ServerRequest serverRequest) {
        log.info("Received request to log in");
        return serverRequest.bodyToMono(LogInDTO.class)
                .flatMap(body -> {
                    String email = body.email();
                    String password =body.password();
                    return logInUseCase.logIn(email, password);
                })
                .flatMap(token -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of("token", token)))
                .doOnSuccess(response -> log.info("User logged in successfully"))
                .doOnError(error -> log.error("Error logging in user: {}", error.getMessage()));
    }


}
