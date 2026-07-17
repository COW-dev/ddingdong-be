package ddingdong.ddingdongBE.domain.calendar.service.dto.query;

import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import java.time.LocalDate;

public record EventQuery(
        Long id,
        String title,
        LocalDate startDate,
        LocalDate endDate,
        RepeatType repeatType,
        String categoryName,
        String color
) {

    public static EventQuery from(Event event) {
        return new EventQuery(
                event.getId(),
                event.getTitle(),
                event.getStartDate(),
                event.getEndDate(),
                event.getRepeatType(),
                event.getCategory().getName(),
                event.getCategory().getColor()
        );
    }
}
