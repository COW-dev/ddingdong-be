package ddingdong.ddingdongBE.domain.event.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EventQrResponse {

    private String uri;

    @Builder
    public EventQrResponse(String uri) {
        this.uri = uri;
    }

    public static EventQrResponse of(String uri) {
        return EventQrResponse.builder()
            .uri(uri)
            .build();
    }
}
