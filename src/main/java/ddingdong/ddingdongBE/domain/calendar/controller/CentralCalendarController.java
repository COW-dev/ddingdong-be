package ddingdong.ddingdongBE.domain.calendar.controller;

import ddingdong.ddingdongBE.domain.calendar.api.CentralCalendarApi;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CalendarResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.EventResponse;
import ddingdong.ddingdongBE.domain.calendar.service.FacadeCentralCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CentralCalendarController implements CentralCalendarApi {

    private final FacadeCentralCalendarService facadeCentralCalendarService;

    @Override
    public CalendarResponse getCalendar(int year, int month) {
        return new CalendarResponse(
                facadeCentralCalendarService.getCalendar(year, month).stream()
                        .map(EventResponse::from)
                        .toList()
        );
    }

}
