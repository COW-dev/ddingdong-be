package ddingdong.ddingdongBE.domain.calendar.api;

import ddingdong.ddingdongBE.domain.calendar.controller.dto.response.CalendarResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Calendar - Club", description = "Club Calendar API")
@RequestMapping("/server/central")
public interface CentralCalendarApi {

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

}
