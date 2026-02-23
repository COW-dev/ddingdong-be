package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class FeedException extends CustomException {

    public FeedException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class CommentNotFoundException extends FeedException {

        private static final String MESSAGE = "존재하지 않는 댓글입니다.";

        public CommentNotFoundException() {
            super(MESSAGE, NOT_FOUND.value());
        }
    }

    public static final class CommentAccessDeniedException extends FeedException {

        private static final String MESSAGE = "댓글을 삭제할 권한이 없습니다.";

        public CommentAccessDeniedException() {
            super(MESSAGE, FORBIDDEN.value());
        }
    }
}
