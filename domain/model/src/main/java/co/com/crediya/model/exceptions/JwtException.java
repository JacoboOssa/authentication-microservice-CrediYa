package co.com.crediya.model.exceptions;


public class JwtException extends RuntimeException {
    public static final String TOKEN_NOT_FOUND = "Token not found";
    public static final String INVALID_TOKEN = "Invalid token";
    public JwtException(String message) {
        super(message);
    }
}

