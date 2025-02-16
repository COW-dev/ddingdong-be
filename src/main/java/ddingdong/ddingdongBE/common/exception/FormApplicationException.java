package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FormApplicationException extends CustomException {

    public static final String FORM_PERIOD_ERROR_MESSAGE = "해당 폼지의 응답기간이 아닙니다";

    public FormApplicationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class FormPeriodException extends FormApplicationException {

        public FormPeriodException() {
            super(FORM_PERIOD_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }
}
