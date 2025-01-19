package ddingdong.ddingdongBE.sse.service.dto;

import java.time.LocalDateTime;

public record SseEvent<T>(
        String eventName,
        T data,
        LocalDateTime timestamp
) {

    public static <T> SseEvent<T> of(String eventName, T data, LocalDateTime timestamp) {
        return new SseEvent<>(eventName, data, timestamp);
    }

}
