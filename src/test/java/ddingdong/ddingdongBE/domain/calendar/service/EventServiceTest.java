package ddingdong.ddingdongBE.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.repository.EventRepository;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @DisplayName("월간 반복 이벤트는 짧은 달을 거쳐도 시작일 기준의 날짜를 유지한다")
    @Test
    void getAllByYearAndMonthDoesNotDriftForMonthlyRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2026, 1, 31),
                LocalDate.of(2026, 5, 31),
                RepeatType.MONTHLY
        );
        given(eventRepository.findAllByPeriod(any(), any())).willReturn(List.of(event));

        // when
        List<EventQuery> result = eventService.getAllByYearAndMonth(2026, 3);

        // then
        assertThat(result).extracting(EventQuery::startDate)
                .containsExactly(LocalDate.of(2026, 3, 31));
    }

    @DisplayName("연간 반복 이벤트는 윤년이 아닌 해를 거쳐도 시작일 기준의 날짜를 유지한다")
    @Test
    void getAllByYearAndMonthDoesNotDriftForYearlyRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2024, 2, 29),
                LocalDate.of(2028, 2, 29),
                RepeatType.YEARLY
        );
        given(eventRepository.findAllByPeriod(any(), any())).willReturn(List.of(event));

        // when
        List<EventQuery> result = eventService.getAllByYearAndMonth(2028, 2);

        // then
        assertThat(result).extracting(EventQuery::startDate)
                .containsExactly(LocalDate.of(2028, 2, 29));
    }

    private Event createEvent(LocalDate startDate, LocalDate endDate, RepeatType repeatType) {
        Category category = Category.builder()
                .name("활동보고서")
                .color("#FFFFFF")
                .build();
        return Event.builder()
                .title("이벤트명")
                .startDate(startDate)
                .endDate(endDate)
                .repeatType(repeatType)
                .category(category)
                .build();
    }
}
