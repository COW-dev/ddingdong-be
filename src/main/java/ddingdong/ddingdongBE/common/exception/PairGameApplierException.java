package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PairGameApplierException extends CustomException {

    private static final String DUPLICATED_PAIR_GAME_APPLIER_MESSAGE = "이미 존재하는 응모자입니다.";
    public PairGameApplierException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class DuplicatedPairGameApplierException extends PairGameApplierException {
        public DuplicatedPairGameApplierException() {
            super(DUPLICATED_PAIR_GAME_APPLIER_MESSAGE, BAD_REQUEST.value());
        }
    }
}
