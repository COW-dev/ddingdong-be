package ddingdong.ddingdongBE.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
    String status,
    String message,
    LocalDateTime timestamp
) {

    public ErrorResponse(String status, String message) {
        this(status, message, LocalDateTime.now());
    }

}
