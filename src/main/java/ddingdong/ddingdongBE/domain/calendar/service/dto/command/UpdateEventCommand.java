package ddingdong.ddingdongBE.domain.calendar.service.dto.command;

import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import java.time.LocalDate;

public record UpdateEventCommand(
        String title,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate repeatEndDate,
        RepeatType repeatType,
        Long categoryId
) {

}
