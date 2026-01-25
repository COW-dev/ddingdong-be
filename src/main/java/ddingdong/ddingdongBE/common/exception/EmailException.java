package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailException extends CustomException {

    private static final String EMAIL_TEMPLATE_NOT_FOUND_MESSAGE = "해당 폼/상태로 발송한 이메일 내용이 없습니다.";
    private static final String NO_EMAIL_RE_SEND_TARGET_MESSAGE = "재전송할 이메일 대상이 없습니다.";


    public EmailException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class EmailTemplateNotFoundException extends EmailException {

        public EmailTemplateNotFoundException() {
            super(EMAIL_TEMPLATE_NOT_FOUND_MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class NoEmailReSendTargetException extends EmailException {

        public NoEmailReSendTargetException() {
            super(NO_EMAIL_RE_SEND_TARGET_MESSAGE, BAD_REQUEST.value());
        }
    }
}
