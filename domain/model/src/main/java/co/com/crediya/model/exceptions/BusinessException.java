package co.com.crediya.model.exceptions;

public class BusinessException extends RuntimeException {
    public static final String EMAIL_ALREADY_IN_USE = "This email is already in use ";
    public static final String IDENTIFICATION_NUMBER_ALREADY_IN_USE = "This identification number is already in use ";
    public static final String ROLE_ID_NOT_FOUND = "Role ID does not exist ";
    public static final String USER_WITH_IDENTIFICATION_NOT_FOUND = "User not found for identification number ";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String BAD_CREDENTIALS = "Email or password are incorrect";
    public BusinessException(String message) {
        super(message);
    }
}
