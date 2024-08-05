package ddingdong.ddingdongBE.domain.event.controller;

import ddingdong.ddingdongBE.common.utils.EventQrGenerator;
import ddingdong.ddingdongBE.domain.event.controller.dto.response.EventQrResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/server/events")
public class EventController {

    private final EventQrGenerator eventQrGenerator;

    @GetMapping("/qr")
    public EventQrResponse generateQrCode(
        @RequestParam("studentName") String studentName,
        @RequestParam("studentNumber") String studentNumber
    ) {
        return eventQrGenerator.generateQrCodeUri(studentName, studentNumber);
    }

}
