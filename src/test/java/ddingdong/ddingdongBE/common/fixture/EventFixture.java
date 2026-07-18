package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import java.time.LocalDate;

public class EventFixture {

    public static Event createEvent(Category category) {
        return createEvent(category, LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 2));
    }

    public static Event createEvent(Category category, LocalDate startDate, LocalDate endDate) {
        return Event.builder()
                .title("testEvent")
                .startDate(startDate)
                .endDate(endDate)
                .repeatType(RepeatType.NONE)
                .category(category)
                .build();
    }
}
