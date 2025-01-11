package ddingdong.ddingdongBE.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

sealed public class SseException extends CustomException {

    public static final String SSE_NOT_FOUND_ERROR_MESSAGE = "SSE Not Found = ";

    public SseException(String message, int errorCode) {
        super(message, errorCode);
    }

    public static final class SseEmitterNotFound extends SseException {

        public SseEmitterNotFound(String sseId) {
            super(SSE_NOT_FOUND_ERROR_MESSAGE + sseId, INTERNAL_SERVER_ERROR.value());
        }
    }
}
