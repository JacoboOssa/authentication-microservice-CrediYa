package co.com.crediya.security.jwt.provider;

import co.com.crediya.model.exceptions.JwtException;
import co.com.crediya.model.rol.Rol;
import co.com.crediya.model.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtProvider {


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

    public JwtProvider(String jwtSecret, Integer jwtExpiration) {
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public Mono<String> generateToken(User user) {
        return Mono.just(
                Jwts.builder()
                        .subject(user.getEmail())
                        .claim("rol", extractRoles(user))
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                        .signWith(getKey(jwtSecret))
                        .compact()
        );
    }

    public Mono<User> validate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            User user = new User();
            user.setEmail(claims.getSubject());

            List<String> roles = claims.get("rol", List.class);
            Rol rol = new Rol();
            if (roles != null && !roles.isEmpty()) {
                rol.setName(roles.get(0));
            }
            user.setRole(rol);


            return Mono.just(user);

        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            return Mono.error(new JwtException(JwtException.TOKEN_EXPIRED));
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            return Mono.error(new JwtException(JwtException.TOKEN_UNSUPPORTED));
        } catch (MalformedJwtException e) {
            log.error(e.getMessage());
            return Mono.error(new JwtException(JwtException.TOKEN_MALFORMED));
        } catch (SignatureException e){
            log.error(e.getMessage());
            return Mono.error(new JwtException(JwtException.INVALID_TOKEN));
        }

        catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return Mono.error(new JwtException(JwtException.ILEGAL_ARGUMENTS));
        }
    }

    private List<String> extractRoles(User user) {
        if (user.getRole() == null) {
            return List.of();
        }
        return List.of(user.getRole().getName());
    }


    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String getSubject(String token) {
        return Jwts.parser()
                .verifyWith(getKey(jwtSecret))
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

}
