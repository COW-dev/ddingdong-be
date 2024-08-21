package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.*;

sealed public class PersistenceException extends CustomException {

    public PersistenceException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class ResourceNotFound extends PersistenceException {

        public ResourceNotFound(String message) {
            super(message, NOT_FOUND.value());
        }
    }
}
