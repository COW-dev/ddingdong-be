package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.CreateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateCategoryCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.CategoryQuery;
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
        return eventService.getAllByYearAndMonth(year, month);
    }

    public EventQuery getEvent(Long eventId) {
        Event event = eventService.getById(eventId);
        return EventQuery.from(event);
    }

    public List<CategoryQuery> getCategories() {
        return categoryService.getAll();
    }

    @Transactional
    public void createEvent(CreateEventCommand command) {
        Category category = categoryService.getById(command.categoryId());
        Event event = command.toEntity(category);
        eventService.save(event);
    }

    @Transactional
    public void updateEvent(Long eventId, UpdateEventCommand command) {
        Event event = eventService.getById(eventId);
        Category category = categoryService.getById(command.categoryId());
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

    @Transactional
    public void updateCategory(Long categoryId, UpdateCategoryCommand command) {
        Category category = categoryService.getById(categoryId);
        categoryService.update(category, command);
    }

    @Transactional
    public void deleteCategory(Long categoryId) {
        categoryService.delete(categoryId);
    }
}
