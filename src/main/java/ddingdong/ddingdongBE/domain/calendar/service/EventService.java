package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.repository.EventRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
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

    public List<EventQuery> getAllByYearAndMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDateOfMonth = yearMonth.atDay(1);
        LocalDate lastDateOfMonth = yearMonth.atEndOfMonth();
        return eventRepository.findAllByPeriod(firstDateOfMonth, lastDateOfMonth).stream()
                .flatMap(event -> expandEvent(event, firstDateOfMonth, lastDateOfMonth).stream())
                .sorted(Comparator.comparing(EventQuery::startDate)
                        .thenComparing(EventQuery::id))
                .toList();
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

    private List<EventQuery> expandEvent(Event event, LocalDate firstDateOfMonth, LocalDate lastDateOfMonth) {
        if (event.getRepeatType() == RepeatType.NONE) {
            return List.of(EventQuery.from(event));
        }

        List<EventQuery> eventQueries = new ArrayList<>();
        LocalDate eventDate = event.getStartDate();
        while (!eventDate.isAfter(event.getEndDate())) {
            if (!eventDate.isBefore(firstDateOfMonth) && !eventDate.isAfter(lastDateOfMonth)) {
                eventQueries.add(toEventQuery(event, eventDate));
            }
            eventDate = getNextEventDate(eventDate, event.getRepeatType());
        }
        return eventQueries;
    }

    private EventQuery toEventQuery(Event event, LocalDate eventDate) {
        return new EventQuery(
                event.getId(),
                event.getTitle(),
                eventDate,
                eventDate,
                event.getRepeatType(),
                event.getCategory().getName(),
                event.getCategory().getColor()
        );
    }

    private LocalDate getNextEventDate(LocalDate eventDate, RepeatType repeatType) {
        return switch (repeatType) {
            case DAILY -> eventDate.plusDays(1);
            case WEEKLY -> eventDate.plusWeeks(1);
            case MONTHLY -> eventDate.plusMonths(1);
            case YEARLY -> eventDate.plusYears(1);
            case NONE -> eventDate;
        };
    }
}
