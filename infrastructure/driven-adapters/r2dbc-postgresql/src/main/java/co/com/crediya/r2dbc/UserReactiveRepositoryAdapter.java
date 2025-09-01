package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.r2dbc.entity.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import co.com.crediya.r2dbc.helper.UserDomainMapper;
import co.com.crediya.r2dbc.helper.UserEntityMapper;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,
        UserEntity,
        String,
        UserReactiveRepository
> implements UserRepository {
    private final UserEntityMapper userEntityMapper;
    private final UserDomainMapper userDomainMapper;

    public UserReactiveRepositoryAdapter(UserReactiveRepository repository, ObjectMapper mapper, UserEntityMapper userEntityMapper, UserDomainMapper userDomainMapper ) {
        super(repository, mapper, userEntityMapper::toDomain);
        this.userEntityMapper = userEntityMapper;
        this.userDomainMapper = userDomainMapper;
    }

    @Override
    protected UserEntity toData(User user) {
        return userEntityMapper.toEntity(user);
    }


    @Override
    public Mono<Boolean> existByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<Boolean> existByIdentificationNumber(String identificationNumber) {
        return repository.existsByIdentificationNumber(identificationNumber) ;
    }

    @Override
    public Mono<String> getEmailByIdentificationNumber(String identificationNumber) {
        return repository.getEmailByIdentificationNumber(identificationNumber);
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .flatMap(userDomainMapper::toDomain);
    }
}
