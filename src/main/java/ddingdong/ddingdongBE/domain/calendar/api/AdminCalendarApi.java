package ddingdong.ddingdongBE.domain.calendar.api;

import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.CreateCategoryRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.CreateEventRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.request.UpdateEventRequest;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CategoriesResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CalendarResponse;
import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.EventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Calendar - Admin", description = "Calendar Admin API")
@RequestMapping("/server/admin/calendar")
public interface AdminCalendarApi {

    @Operation(summary = "캘린더 조회 API")
    @ApiResponse(responseCode = "200", description = "캘린더 조회 성공",
            content = @Content(schema = @Schema(implementation = CalendarResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping
    CalendarResponse getCalendar(
            @RequestParam("year") int year,
            @RequestParam("month") int month
    );

    @Operation(summary = "이벤트 상세조회 API")
    @ApiResponse(responseCode = "200", description = "이벤트 상세조회 성공",
            content = @Content(schema = @Schema(implementation = EventResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/events/{eventId}")
    EventResponse getEvent(@PathVariable("eventId") Long eventId);

    @Operation(summary = "카테고리 목록 조회 API")
    @ApiResponse(responseCode = "200", description = "카테고리 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = CategoriesResponse.class)))
    @ResponseStatus(HttpStatus.OK)
    @SecurityRequirement(name = "AccessToken")
    @GetMapping("/categories")
    CategoriesResponse getCategories();

    @Operation(summary = "이벤트 생성 API")
    @ApiResponse(responseCode = "201", description = "이벤트 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/events")
    void createEvent(@Valid @RequestBody CreateEventRequest request);

    @Operation(summary = "이벤트 수정 API")
    @ApiResponse(responseCode = "204", description = "이벤트 수정 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @PutMapping("/events/{eventId}")
    void updateEvent(
            @PathVariable("eventId") Long eventId,
            @Valid @RequestBody UpdateEventRequest request
    );

    @Operation(summary = "이벤트 삭제 API")
    @ApiResponse(responseCode = "204", description = "이벤트 삭제 성공")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "AccessToken")
    @DeleteMapping("/events/{eventId}")
    void deleteEvent(@PathVariable("eventId") Long eventId);

    @Operation(summary = "카테고리 생성 API")
    @ApiResponse(responseCode = "201", description = "카테고리 생성 성공")
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "AccessToken")
    @PostMapping("/category")
    void createCategory(@Valid @RequestBody CreateCategoryRequest request);

}
