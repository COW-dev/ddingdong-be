package ddingdong.ddingdongBE.common.exception;

import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException(RuntimeException e) {
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e) {
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(IllegalArgumentException e) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNoSuchElementException(NoSuchElementException e) {
        return ExceptionResponse.of(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {
        return switch (e.getErrorMessage()) {
            case INVALID_PASSWORD, UNREGISTER_ID -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ExceptionResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage()));

            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage()));
        };
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}