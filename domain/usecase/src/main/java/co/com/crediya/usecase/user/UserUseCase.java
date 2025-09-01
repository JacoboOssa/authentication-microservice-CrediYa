package co.com.crediya.usecase.user;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.AuthRepository;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserUseCase {
   private final UserRepository userRepository;
   private final RolRepository rolRepository;
   private final AuthRepository authRepository;

    public UserUseCase(UserRepository userRepository, RolRepository rolRepository, AuthRepository authRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
        this.authRepository = authRepository;
    }

    public Mono<User> createUser(User user) {
        return validateIfEmailExists(user.getEmail())
                .then(validateIfIdentificationNumberExists(user.getIdentificationNumber()))
                .then(validateIfRoleIdExists(user.getRole().getId()))
                .then(rolRepository.findById(user.getRole().getId()))
                .map(role -> {
                    user.setRole(role);
                    user.setPassword(authRepository.hashPassword(user.getPassword()));
                    return user;
                })
                .flatMap(userRepository::save);
    }


    public Mono<Void> validateIfEmailExists(String email) {
        return userRepository.existByEmail(email)
                .filter(emailExists -> !emailExists)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.EMAIL_ALREADY_IN_USE + email)))
                .then();
    }

    public Mono<Void> validateIfIdentificationNumberExists(String identificationNumber) {
        return userRepository.existByIdentificationNumber(identificationNumber)
                .filter(idExists -> !idExists)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.IDENTIFICATION_NUMBER_ALREADY_IN_USE + identificationNumber)))
                .then();
    }

    public Mono<Void> validateIfRoleIdExists(String roleId) {
        return rolRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessException.ROLE_ID_NOT_FOUND + roleId)))
                .then();
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll()
                .flatMap(user ->
                        rolRepository.findById(user.getRole().getId())
                                .map(role -> {
                                    user.setRole(role);
                                    return user;
                                })
                );
    }

    public Mono<String> getEmailByIdentificationNumber(String identificationNumber) {
        return userRepository.getEmailByIdentificationNumber(identificationNumber)
                .switchIfEmpty(Mono.error(new BusinessException(
                        BusinessException.USER_WITH_IDENTIFICATION_NOT_FOUND + identificationNumber
                )));
    }









}
