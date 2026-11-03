package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ActivityReportException extends CustomException {

    public ActivityReportException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class OverlapTermPeriodException extends ActivityReportException {

        private static final String MESSAGE = "회차 시작일은 기존 회차 마감일 이후여야 합니다.";

        public OverlapTermPeriodException() {
            super(MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class TermPeriodExceedsYearException extends ActivityReportException {

        private static final String MESSAGE = "회차 기간은 해를 넘길 수 없습니다.";

        public TermPeriodExceedsYearException() {
            super(MESSAGE, BAD_REQUEST.value());
        }
    }
}
