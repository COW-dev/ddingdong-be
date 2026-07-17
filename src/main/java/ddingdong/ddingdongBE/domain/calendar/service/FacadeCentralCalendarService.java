package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeCentralCalendarService {

    private final EventService eventService;

    public List<EventQuery> getCalendar(int year, int month) {
        return eventService.getAllByYearAndMonth(year, month).stream()
                .map(EventQuery::from)
                .toList();
    }

}
