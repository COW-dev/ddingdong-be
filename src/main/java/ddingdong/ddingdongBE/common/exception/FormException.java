package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class FormException extends CustomException {

    public static final String FORM_PERIOD_ERROR_MESSAGE = "해당 폼지의 응답기간이 아닙니다";
    private static final String OVERLAP_FORM_DATE_MESSAGE = "입력한 기간과 겹치는 폼이 존재합니다.";
    private static final String INVALID_FIELD_TYPE_MESSAGE = "통계를 조회할 질문 유형이 올바르지 않습니다.";
    private static final String INVALID_FORM_DATE_MESSAGE = "폼지 종료일자는 폼지 시작일자 이전일 수 없습니다.";


    public FormException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class FormPeriodException extends FormException {

        public FormPeriodException() {
            super(FORM_PERIOD_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class OverlapFormPeriodException extends FormException {

        public OverlapFormPeriodException() {
            super(OVERLAP_FORM_DATE_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class InvalidFormEndDateException extends FormException {

        public InvalidFormEndDateException() {
            super(INVALID_FORM_DATE_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class InvalidFieldTypeException extends FormException {

        public InvalidFieldTypeException() {
            super(INVALID_FIELD_TYPE_MESSAGE, BAD_REQUEST.value());
        }
    }
}
