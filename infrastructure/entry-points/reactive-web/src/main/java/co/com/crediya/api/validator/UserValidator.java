package co.com.crediya.api.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class UserValidator {

    private final Validator validator;

    public UserValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validate(T object) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(object);
            if (!violations.isEmpty())
                return Mono.error(new ConstraintViolationException(violations));
            return Mono.just(object);
        });
    }

    public <T> Mono<T> validate(T object, Class<?>... groups) {
        return Mono.defer(() -> {
            Set<ConstraintViolation<T>> violations = validator.validate(object, groups);
            if (!violations.isEmpty())
                return Mono.error(new ConstraintViolationException(violations));
            return Mono.just(object);
        });
    }
 }
