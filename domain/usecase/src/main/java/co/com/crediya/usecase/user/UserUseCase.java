package co.com.crediya.usecase.user;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.rol.gateways.RolRepository;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

public class UserUseCase {
   private final UserRepository userRepository;
   private final RolRepository rolRepository;

    public UserUseCase(UserRepository userRepository, RolRepository rolRepository) {
        this.userRepository = userRepository;
        this.rolRepository = rolRepository;
    }

    public Mono<User> createUser(User user) {
        return userRepository.existByEmail(user.getEmail())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new BusinessException("This email is already in use: " + user.getEmail()));
                    }
                    return userRepository.existByIdentificationNumber(user.getIdentificationNumber())
                            .flatMap(idExists -> {
                                if (idExists) {
                                    return Mono.error(new BusinessException("This identification number is already in use: " + user.getIdentificationNumber()));
                                }
                                return userRepository.save(user)
                                        .flatMap(savedUser ->
                                                rolRepository.findById(savedUser.getRole().getId())
                                                        .map(role -> {
                                                            savedUser.setRole(role);
                                                            return savedUser;
                                                        })
                                        );
                            });
                });
    }



}
