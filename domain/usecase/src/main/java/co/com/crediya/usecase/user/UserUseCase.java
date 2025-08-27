package co.com.crediya.usecase.user;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class UserUseCase {
   private final UserRepository userRepository;
   private final RolRepository rolRepository;

    public UserUseCase(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    public Mono<User> createUser(User user) {
        return validateIfEmailExists(user.getEmail())
                .then(validateIfIdentificationNumberExists(user.getIdentificationNumber()))
                .then(validateIfRoleIdExists(user.getRole().getId()))
                .then(rolRepository.findById(user.getRole().getId()))
                .map(role -> {
                    user.setRole(role);
                    return user;
                })
                .flatMap(userRepository::save);
    }


    public Mono<Void> validateIfEmailExists(String email) {
        return userRepository.existByEmail(email)
                .filter(emailExists -> !emailExists)
                .switchIfEmpty(Mono.error(new BusinessException("This email is already in use: " + email)))
                .then();
    }

    public Mono<Void> validateIfIdentificationNumberExists(String identificationNumber) {
        return userRepository.existByIdentificationNumber(identificationNumber)
                .filter(idExists -> !idExists)
                .switchIfEmpty(Mono.error(new BusinessException("This identification number is already in use: " + identificationNumber)))
                .then();
    }

    public Mono<Void> validateIfRoleIdExists(String roleId) {
        return rolRepository.findById(roleId)
                .switchIfEmpty(Mono.error(new BusinessException("Role ID does not exist: " + roleId)))
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
                        "User not found for identification number: " + identificationNumber
                )));
    }









}
