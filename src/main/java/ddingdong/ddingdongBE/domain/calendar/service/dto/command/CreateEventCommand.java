
package ddingdong.ddingdongBE.domain.calendar.service.dto.command;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import java.time.LocalDate;

public record CreateEventCommand(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        RepeatType repeatType,
        String categoryName,
        String color
) {

    public Event toEntity(Category category) {
        return Event.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .repeatType(repeatType)
                .category(category)
                .build();
    }
}
