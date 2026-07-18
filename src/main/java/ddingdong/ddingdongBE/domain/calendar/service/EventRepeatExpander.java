package ddingdong.ddingdongBE.domain.calendar.service;

import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class EventRepeatExpander {

    public List<EventQuery> expand(List<Event> events, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDateOfMonth = yearMonth.atDay(1);
        LocalDate lastDateOfMonth = yearMonth.atEndOfMonth();

        return events.stream()
                .flatMap(event -> expand(event, firstDateOfMonth, lastDateOfMonth).stream())
                .sorted(Comparator.comparing(EventQuery::startDate)
                        .thenComparing(EventQuery::id))
                .toList();
    }

    private List<EventQuery> expand(Event event, LocalDate firstDateOfMonth, LocalDate lastDateOfMonth) {
        if (event.getRepeatType() == RepeatType.NONE) {
            return List.of(EventQuery.from(event));
        }

        List<EventQuery> occurrences = new ArrayList<>();
        LocalDate occurrenceDate = event.getStartDate();
        while (!occurrenceDate.isAfter(event.getEndDate())) {
            if (!occurrenceDate.isBefore(firstDateOfMonth) && !occurrenceDate.isAfter(lastDateOfMonth)) {
                occurrences.add(toOccurrenceQuery(event, occurrenceDate));
            }
            occurrenceDate = nextOccurrenceDate(occurrenceDate, event.getRepeatType());
        }
        return occurrences;
    }

    private EventQuery toOccurrenceQuery(Event event, LocalDate occurrenceDate) {
        return new EventQuery(
                event.getId(),
                event.getTitle(),
                occurrenceDate,
                occurrenceDate,
                event.getRepeatType(),
                event.getCategory().getName(),
                event.getCategory().getColor()
        );
    }

    private LocalDate nextOccurrenceDate(LocalDate occurrenceDate, RepeatType repeatType) {
        return switch (repeatType) {
            case DAILY -> occurrenceDate.plusDays(1);
            case WEEKLY -> occurrenceDate.plusWeeks(1);
            case MONTHLY -> occurrenceDate.plusMonths(1);
            case YEARLY -> occurrenceDate.plusYears(1);
            case NONE -> occurrenceDate;
        };
    }
}
