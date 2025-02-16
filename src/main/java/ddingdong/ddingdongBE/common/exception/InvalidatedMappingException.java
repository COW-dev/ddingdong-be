package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class InvalidatedMappingException extends CustomException {

    public InvalidatedMappingException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class InvalidatedEnumValue extends InvalidatedMappingException {

        public InvalidatedEnumValue(String message) {
            super(message, BAD_REQUEST.value());
        }
    }

}
