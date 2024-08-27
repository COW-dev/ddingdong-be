package ddingdong.ddingdongBE.common.exception;

import lombok.Getter;

@Getter
abstract class CustomException extends RuntimeException {
    private final String message;
    private final int errorCode;

    public CustomException(String message, int errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

}
