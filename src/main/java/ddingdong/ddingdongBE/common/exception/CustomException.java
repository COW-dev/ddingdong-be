package ddingdong.ddingdongBE.common.exception;

abstract class CustomException extends RuntimeException {
    String message;
    int errorCode;

    public CustomException(String message, int errorCode) {
        super(message);
        this.message = message;
        this.errorCode = errorCode;
    }

}
