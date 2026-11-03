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

    public static final class NotSubmissionPeriodException extends ActivityReportException {

        private static final String MESSAGE = "활동보고서 제출 기간이 아닙니다.";

        public NotSubmissionPeriodException() {
            super(MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class SubmittedTermDeleteException extends ActivityReportException {

        private static final String MESSAGE = "제출된 활동보고서가 있어 회차를 삭제할 수 없습니다.";

        public SubmittedTermDeleteException() {
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
