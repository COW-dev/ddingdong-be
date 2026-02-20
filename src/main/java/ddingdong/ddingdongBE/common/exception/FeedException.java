package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

public class FeedException extends CustomException {

    public FeedException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class DuplicatedFeedLikeException extends FeedException {

        private static final String MESSAGE = "이미 좋아요한 피드입니다.";

        public DuplicatedFeedLikeException() {
            super(MESSAGE, BAD_REQUEST.value());
        }
    }

    public static final class FeedCommentAccessDeniedException extends FeedException {

        private static final String MESSAGE = "댓글을 삭제할 권한이 없습니다.";

        public FeedCommentAccessDeniedException() {
            super(MESSAGE, FORBIDDEN.value());
        }
    }
}
