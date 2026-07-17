package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminCalendarService {

    private final EventService eventService;
    private final CategoryService categoryService;

    public List<EventQuery> getCalendar(int year, int month) {
        return eventService.getAllByYearAndMonth(year, month).stream()
                .map(EventQuery::from)
                .toList();
    }

    public EventQuery getEvent(Long eventId) {
        Event event = eventService.getById(eventId);
        return EventQuery.from(event);
    }

    @Transactional
    public void createEvent(CreateEventCommand command) {
        Category category = categoryService.getByName(command.categoryName());
        Event event = command.toEntity(category);
        eventService.save(event);
    }

    @Transactional
    public void updateEvent(Long eventId, UpdateEventCommand command) {
        Event event = eventService.getById(eventId);
        Category category = categoryService.getByName(command.categoryName());
        eventService.update(event, command, category);
    }

    @Transactional
    public void deleteEvent(Long eventId) {
        eventService.delete(eventId);
    }

    @Transactional
    public void createCategory(CreateCategoryCommand command) {
        Category category = command.toEntity();
        categoryService.save(category);
    }
}
