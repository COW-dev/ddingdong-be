package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class InvalidatedMappingException extends CustomException {

    private static final String INVALID_FORM_DATE_MESSAGE = "입력한 기간과 겹치는 폼이 존재합니다.";

    public InvalidatedMappingException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class InvalidatedEnumValue extends InvalidatedMappingException {

        public InvalidatedEnumValue(String message) {
            super(message, BAD_REQUEST.value());
        }
    }

    public static final class InvalidFormPeriodException extends InvalidatedMappingException {

        public InvalidFormPeriodException() {
            super(INVALID_FORM_DATE_MESSAGE, BAD_REQUEST.value());
        }
    }
}
