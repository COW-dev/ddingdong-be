
package ddingdong.ddingdongBE.domain.calendar.service.dto.command;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import java.time.LocalDate;

public record CreateEventCommand(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate repeatEndDate,
        RepeatType repeatType,
        String categoryName
) {

    public Event toEntity(Category category) {
        return Event.builder()
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .repeatEndDate(repeatEndDate)
                .repeatType(repeatType)
                .category(category)
                .build();
    }
}
