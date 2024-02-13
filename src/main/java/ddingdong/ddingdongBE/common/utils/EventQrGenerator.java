package ddingdong.ddingdongBE.common.utils;

import ddingdong.ddingdongBE.domain.event.controller.dto.response.EventQrResponse;
import org.springframework.stereotype.Component;

@Component
public class EventQrGenerator {

    public static final String URI_PREFIX = "https://chart.apis.google.com/chart?cht=qr&chs=250x250&chl=";

    public EventQrResponse generateQrCodeUri(String studentName, String studentNumber) {
        String uri = URI_PREFIX + "https://ddingdong.club/event?studentName=" + studentName + "%26" + "studentNumber=" + studentNumber;

        return EventQrResponse.of(uri);
    }
}
