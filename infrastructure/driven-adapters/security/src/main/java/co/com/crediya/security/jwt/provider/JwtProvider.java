package co.com.crediya.security.jwt.provider;

import co.com.crediya.model.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


@Component
public class JwtProvider {

    private static final Logger LOGGER =  Logger.getLogger(JwtProvider.class.getName());


    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Integer jwtExpiration;

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

    public boolean validate(String token){
        try {
            Jwts.parser()
                    .verifyWith(getKey(jwtSecret))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            return true;
        } catch (ExpiredJwtException e) {
            LOGGER.severe("token expired");
        } catch (UnsupportedJwtException e) {
            LOGGER.severe("token unsupported");
        } catch (MalformedJwtException e) {
            LOGGER.severe("token malformed");
        } catch (IllegalArgumentException e) {
            LOGGER.severe("illegal args");
        }
        return false;
    }

    private SecretKey getKey(String secret) {
        byte[] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

}
