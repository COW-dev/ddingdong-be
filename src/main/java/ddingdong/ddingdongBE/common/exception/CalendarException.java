package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CalendarException extends CustomException {

    private static final String INVALID_EVENT_PERIOD_MESSAGE = "이벤트 종료일은 시작일보다 빠를 수 없습니다.";
    private static final String INVALID_REPEAT_END_DATE_MESSAGE = "반복 종료일은 이벤트 종료일보다 빠를 수 없습니다.";
    private static final String DUPLICATED_CATEGORY_NAME_MESSAGE = "이미 존재하는 카테고리명입니다.";

    public CalendarException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class InvalidEventPeriodException extends CalendarException {

        public InvalidEventPeriodException() {
            super(INVALID_EVENT_PERIOD_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class InvalidRepeatEndDateException extends CalendarException {

        public InvalidRepeatEndDateException() {
            super(INVALID_REPEAT_END_DATE_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class DuplicatedCategoryNameException extends CalendarException {

        public DuplicatedCategoryNameException() {
            super(DUPLICATED_CATEGORY_NAME_MESSAGE, BAD_REQUEST.value());
        }
    }
}
