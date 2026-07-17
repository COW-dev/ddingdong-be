package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.repository.EventRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    @Transactional
    public Long save(Event event) {
        Event savedEvent = eventRepository.save(event);
        return savedEvent.getId();
    }

    public Event getById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFound("Event(eventId=" + eventId + ")를 찾을 수 없습니다."));
    }

    public List<Event> getAllByYearAndMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDateOfMonth = yearMonth.atDay(1);
        LocalDate lastDateOfMonth = yearMonth.atEndOfMonth();
        return eventRepository.findAllByPeriod(firstDateOfMonth, lastDateOfMonth);
    }

    @Transactional
    public void update(Event event, UpdateEventCommand command, Category category) {
        event.update(command, category);
    }

    @Transactional
    public void delete(Long eventId) {
        Event event = getById(eventId);
        eventRepository.delete(event);
    }
}
