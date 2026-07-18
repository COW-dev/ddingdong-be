package ddingdong.ddingdongBE.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.domain.calendar.entity.Category;
import ddingdong.ddingdongBE.domain.calendar.entity.Event;
import ddingdong.ddingdongBE.domain.calendar.entity.RepeatType;
import ddingdong.ddingdongBE.domain.calendar.service.dto.query.EventQuery;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventRepeatExpanderTest {

    private final EventRepeatExpander eventRepeatExpander = new EventRepeatExpander();

    @DisplayName("일간 반복 이벤트를 조회 월의 발생일별로 반환한다")
    @Test
    void expandDailyRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2026, 7, 29),
                LocalDate.of(2026, 8, 2),
                RepeatType.DAILY
        );

        // when
        List<EventQuery> result = eventRepeatExpander.expand(List.of(event), 2026, 7);

        // then
        assertThat(result).extracting(EventQuery::startDate)
                .containsExactly(
                        LocalDate.of(2026, 7, 29),
                        LocalDate.of(2026, 7, 30),
                        LocalDate.of(2026, 7, 31)
                );
        assertThat(result).extracting(EventQuery::endDate)
                .containsExactly(
                        LocalDate.of(2026, 7, 29),
                        LocalDate.of(2026, 7, 30),
                        LocalDate.of(2026, 7, 31)
                );
    }

    @DisplayName("월간 반복 이벤트를 조회 월의 발생일별로 반환한다")
    @Test
    void expandMonthlyRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2026, 5, 15),
                LocalDate.of(2026, 8, 15),
                RepeatType.MONTHLY
        );

        // when
        List<EventQuery> result = eventRepeatExpander.expand(List.of(event), 2026, 7);

        // then
        assertThat(result).extracting(EventQuery::startDate)
                .containsExactly(LocalDate.of(2026, 7, 15));
        assertThat(result).extracting(EventQuery::endDate)
                .containsExactly(LocalDate.of(2026, 7, 15));
    }

    @DisplayName("연간 반복 이벤트를 조회 월의 발생일별로 반환한다")
    @Test
    void expandYearlyRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2024, 7, 18),
                LocalDate.of(2026, 7, 18),
                RepeatType.YEARLY
        );

        // when
        List<EventQuery> result = eventRepeatExpander.expand(List.of(event), 2026, 7);

        // then
        assertThat(result).extracting(EventQuery::startDate)
                .containsExactly(LocalDate.of(2026, 7, 18));
        assertThat(result).extracting(EventQuery::endDate)
                .containsExactly(LocalDate.of(2026, 7, 18));
    }

    @DisplayName("반복이 없는 이벤트는 기존 기간을 유지한다")
    @Test
    void expandNoneRepeatEvent() {
        // given
        Event event = createEvent(
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 3),
                RepeatType.NONE
        );

        // when
        List<EventQuery> result = eventRepeatExpander.expand(List.of(event), 2026, 7);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).startDate()).isEqualTo(LocalDate.of(2026, 7, 1));
        assertThat(result.get(0).endDate()).isEqualTo(LocalDate.of(2026, 7, 3));
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
