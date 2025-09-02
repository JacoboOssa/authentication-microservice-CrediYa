package co.com.crediya.api.exceptionhandler;

import java.util.LinkedHashMap;
import java.util.Map;

import co.com.crediya.model.exceptions.BusinessException;
import co.com.crediya.model.exceptions.JwtException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;


@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ATTRIBUTE_STATUS = "status";
    private static final String VALIDATION_ERROR = "Validation Error";
    private static final String BUSINESSVIOLATION_ERROR = "Business Rule Violation";
    private static final String JWT_ERROR = "JWT Validation Error";



    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(request);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("Method", request.method().name());
        errorAttributes.put("path", request.path());


        if (error instanceof ConstraintViolationException) {
            errorAttributes.put(ATTRIBUTE_ERROR, VALIDATION_ERROR);
            errorAttributes.put(ATTRIBUTE_STATUS, HttpStatus.BAD_REQUEST.value());
        } else if (error instanceof BusinessException) {
            errorAttributes.put(ATTRIBUTE_ERROR, BUSINESSVIOLATION_ERROR);
            errorAttributes.put(ATTRIBUTE_STATUS, HttpStatus.CONFLICT.value());
        } else if (error instanceof JwtException) {
            errorAttributes.put(ATTRIBUTE_ERROR, JWT_ERROR);
            errorAttributes.put(ATTRIBUTE_STATUS, HttpStatus.UNAUTHORIZED.value());
        } else {
            errorAttributes.put(ATTRIBUTE_ERROR, "Internal Server Error");
            errorAttributes.put(ATTRIBUTE_STATUS, HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return errorAttributes;
    }
}
