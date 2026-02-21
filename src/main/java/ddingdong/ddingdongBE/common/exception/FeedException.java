package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

public class FeedException extends CustomException {

    public FeedException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class DuplicatedFeedLikeException extends FeedException {

        private static final String MESSAGE = "이미 좋아요한 피드입니다.";

        public DuplicatedFeedLikeException() {
            super(MESSAGE, CONFLICT.value());
        }
    }

    public static final class FeedLikeNotFoundException extends FeedException {

        private static final String MESSAGE = "좋아요 기록이 존재하지 않습니다.";

        public FeedLikeNotFoundException() {
            super(MESSAGE, NOT_FOUND.value());
        }
    }

    public static final class FeedCommentAccessDeniedException extends FeedException {

        private static final String MESSAGE = "댓글을 삭제할 권한이 없습니다.";

        public FeedCommentAccessDeniedException() {
            super(MESSAGE, FORBIDDEN.value());
        }
    }
}
