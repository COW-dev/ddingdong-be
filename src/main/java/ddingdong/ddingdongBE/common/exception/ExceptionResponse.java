package ddingdong.ddingdongBE.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// TODO : 모든 에러 전환 후에 삭제 필요
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

    private final int code;
    private final String message;

    public static ExceptionResponse of(HttpStatus httpStatus, String message) {
        return new ExceptionResponse(httpStatus.value(), message);
    }
}
