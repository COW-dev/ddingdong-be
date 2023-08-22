package ddingdong.ddingdongBE.common.exception;

import static ddingdong.ddingdongBE.common.exception.ErrorMessage.*;

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
        e.printStackTrace();
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getText());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleException(Exception e) {
        e.printStackTrace();
        return ExceptionResponse.of(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR.getText());
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
            case AUTHENTICATION_FAILURE, UNREGISTER_USER -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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
