package ddingdong.ddingdongBE.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

    private final int code;
    private final String message;

    public static ExceptionResponse of(HttpStatus httpStatus, String message) {
        return new ExceptionResponse(httpStatus.value(), message);
    }
}
