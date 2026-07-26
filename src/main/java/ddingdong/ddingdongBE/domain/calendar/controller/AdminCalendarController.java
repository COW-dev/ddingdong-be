package ddingdong.ddingdongBE.domain.calendar.controller;

import ddingdong.ddingdongBE.domain.calendar.api.AdminCalendarApi;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.CreateCategoryRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.CreateEventRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.UpdateCategoryRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.UpdateEventRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CategoriesResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CategoryResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CalendarResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.EventResponse;
import ddingdong.ddingdongBE.domain.calendar.service.FacadeAdminCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminCalendarController implements AdminCalendarApi {

    private final FacadeAdminCalendarService facadeAdminCalendarService;

    @Override
    public CalendarResponse getCalendar(int year, int month) {
        return new CalendarResponse(
                facadeAdminCalendarService.getCalendar(year, month).stream()
                        .map(EventResponse::from)
                        .toList()
        );
    }

    @Override
    public EventResponse getEvent(Long eventId) {
        return EventResponse.from(facadeAdminCalendarService.getEvent(eventId));
    }

    @Override
    public CategoriesResponse getCategories() {
        return new CategoriesResponse(
                facadeAdminCalendarService.getCategories().stream()
                        .map(CategoryResponse::from)
                        .toList()
        );
    }

    @Override
    public void createEvent(CreateEventRequest request) {
        facadeAdminCalendarService.createEvent(request.toCommand());
    }

    @Override
    public void updateEvent(Long eventId, UpdateEventRequest request) {
        facadeAdminCalendarService.updateEvent(eventId, request.toCommand());
    }

    @Override
    public void deleteEvent(Long eventId) {
        facadeAdminCalendarService.deleteEvent(eventId);
    }

    @Override
    public void createCategory(CreateCategoryRequest request) {
        facadeAdminCalendarService.createCategory(request.toCommand());
    }

    @Override
    public void updateCategory(Long categoryId, UpdateCategoryRequest request) {
        facadeAdminCalendarService.updateCategory(categoryId, request.toCommand());
    }

    @Override
    public void deleteCategory(Long categoryId) {
        facadeAdminCalendarService.deleteCategory(categoryId);
    }
}
