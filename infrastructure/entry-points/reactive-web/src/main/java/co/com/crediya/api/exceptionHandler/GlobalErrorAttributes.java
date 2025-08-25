package co.com.crediya.api.exceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

import co.com.crediya.model.exceptions.BusinessException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable error = getError(request);
        errorAttributes.put("message", error.getMessage());
        errorAttributes.put("Method", request.method().name());
        errorAttributes.put("path", request.path());


        if (error instanceof ConstraintViolationException) {
            errorAttributes.put("error", "Validation Error");
            errorAttributes.put("status", HttpStatus.BAD_REQUEST.value());
        } else if (error instanceof BusinessException) {
            errorAttributes.put("error", "Business Rule Violation");
            errorAttributes.put("status", HttpStatus.CONFLICT.value());
        } else {
            errorAttributes.put("error", "Internal Server Error");
            errorAttributes.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return errorAttributes;
    }
}
