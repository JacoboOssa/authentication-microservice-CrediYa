package co.com.crediya.api;

import co.com.crediya.api.dto.request.CreateUserRequestDTO;
import co.com.crediya.api.mapper.UserDTOMapper;
import co.com.crediya.api.validator.UserValidator;
import co.com.crediya.transaction.TransactionalAdapter;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {
    private final UserUseCase userUseCase;
    private final UserDTOMapper userDTOMapper;
    private final UserValidator userValidator;
    private final TransactionalAdapter transactionalAdapter;



    public Mono<ServerResponse> listenSaveUser(ServerRequest serverRequest) {
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

}
