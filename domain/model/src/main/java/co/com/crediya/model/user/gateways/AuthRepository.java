package co.com.crediya.model.user.gateways;
import co.com.crediya.model.user.User;
import reactor.core.publisher.Mono;

public interface AuthRepository {
    String hashPassword(String rawPassword);
    boolean verifyPassword(String rawPassword, String hashedPassword);
    Mono<String> generateToken(User user);
}
