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

    public static final class FeedRankingNotFoundException extends FeedException {

        private static final String MESSAGE = "해당 연도에 피드가 존재하지 않습니다.";

        public FeedRankingNotFoundException() {
            super(MESSAGE, NOT_FOUND.value());
        }
    }
}
