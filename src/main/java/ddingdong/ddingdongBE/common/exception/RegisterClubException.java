package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

sealed public class RegisterClubException extends CustomException {

    public static final String ALREADY_EXIST_CLUB_ID_ERROR_MESSAGE = "이미 존재하는 동아리 계정입니다.";

    public RegisterClubException(String message, int errorCode) {
        super(message, errorCode);
    }


    public static final class AlreadyExistClubId extends RegisterClubException {

        public AlreadyExistClubId() {
            super(ALREADY_EXIST_CLUB_ID_ERROR_MESSAGE, BAD_REQUEST.value());
        }
    }
}
