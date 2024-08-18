package ddingdong.ddingdongBE.common.exception;

import io.swagger.v3.oas.annotations.Hidden;
import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@Hidden
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class CustomExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.class)
    public ErrorResponse handleCustomException(
        CustomException exception,
        HttpServletRequest request
    ) {
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
            : request.getRemoteAddr();

        log.warn(
            requestMethod + requestUrl + queryString + " from ip: " + clientIp
                + "\n"
                + HttpStatus.BAD_REQUEST.value() + " : " + exception.message
        );

        return new ErrorResponse(
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            exception.message
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse handleIllegalArgumentException(
        IllegalArgumentException exception,
        HttpServletRequest request
    ) {
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
            : request.getRemoteAddr();

        log.warn(
            requestMethod + requestUrl + queryString + " from ip: " + clientIp
                + "\n"
                + exception.getClass().getSimpleName() + " : " + exception.getMessage()
        );

        return new ErrorResponse(
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            exception.getMessage()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception,
        HttpServletRequest request
    ) {
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
            : request.getRemoteAddr();

        String message = exception.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(FieldError::getDefaultMessage)
            .orElse("입력된 값이 올바르지 않습니다.");

        log.warn(
            requestMethod + requestUrl + queryString + " from ip: " + clientIp
                + "\n"
                + exception.getClass().getSimpleName() + " : " + message
        );

        return new ErrorResponse(
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            message
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Throwable.class)
    public ErrorResponse handleSystemException(
        Throwable exception,
        HttpServletRequest request
    ) {
        String requestMethod = request.getMethod();
        String requestUrl = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
            : request.getRemoteAddr();

        log.warn(
            requestMethod + requestUrl + queryString + " from ip: " + clientIp
                + "\n"
                + "[SYSTEM-ERROR]" + " : " + exception.getMessage()
        );

        return new ErrorResponse(
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            "Internal Sever Error"
        );
    }

    // TODO : 해당 에러 대신 PersistenceException.NotFound()로 전환 필요
    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleNoSuchElementException(NoSuchElementException e) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // TODO : 커스텀 exception으로 변경 필요
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationException(AuthenticationException e) {
        return switch (e.getErrorMessage()) {
            case INVALID_PASSWORD, UNREGISTER_ID -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ExceptionResponse.of(HttpStatus.UNAUTHORIZED, e.getMessage()));

            default -> ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage()));
        };
    }

    // TODO : presigned url 도입 시, 삭제 예정
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleMissingServletRequestPartException(MissingServletRequestPartException e) {
        return ExceptionResponse.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
