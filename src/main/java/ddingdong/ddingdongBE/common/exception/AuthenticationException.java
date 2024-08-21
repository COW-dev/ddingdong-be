package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

sealed public class AuthenticationException extends CustomException {

    public AuthenticationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class UnRegisteredId extends AuthenticationException {

        public UnRegisteredId(String message) {
            super(message, UNAUTHORIZED.value());
        }
    }

    public static final class InvalidPassword extends AuthenticationException {

        public InvalidPassword(String message) {
            super(message, UNAUTHORIZED.value());
        }
    }
}
