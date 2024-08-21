package ddingdong.ddingdongBE.common.exception;

abstract class CustomException extends RuntimeException {
    String message;

    public CustomException(String message) {
        this.message = message;
    }

}
