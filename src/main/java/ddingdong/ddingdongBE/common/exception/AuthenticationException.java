package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

sealed public class AuthenticationException extends CustomException {

    public static final String UNREGISTERED_ID_ERROR_MESSAGE = "등록되지 않은 ID입니다.";
    public static final String INVALIDATED_PASSWORD_ERROR_MESSAGE = "잘못된 비밀번호입니다.";
    public static final String NON_EXIST_USER_ROLE_ERROR_MESSAGE = "유저 권한이 존재하지 않습니다.";
    public static final String NON_HAVE_AUTHORITY_MESSAGE = "해당 유저에게 권한이 없습니다.";

    public AuthenticationException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class UnRegisteredId extends AuthenticationException {

        public UnRegisteredId() {
            super(UNREGISTERED_ID_ERROR_MESSAGE, UNAUTHORIZED.value());
        }
    }

    public static final class InvalidPassword extends AuthenticationException {

        public InvalidPassword() {
            super(INVALIDATED_PASSWORD_ERROR_MESSAGE, UNAUTHORIZED.value());
        }
    }

    public static final class NonExistUserRole extends AuthenticationException {

        public NonExistUserRole() {
            super(NON_EXIST_USER_ROLE_ERROR_MESSAGE, UNAUTHORIZED.value());
        }
    }

    public static final class NonHaveAuthority extends AuthenticationException {

        public NonHaveAuthority() {
            super(NON_HAVE_AUTHORITY_MESSAGE, UNAUTHORIZED.value());
        }
    }
}
