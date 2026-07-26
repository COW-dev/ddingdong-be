package ddingdong.ddingdongBE.domain.calendar.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ddingdong.ddingdongBE.common.exception.CalendarException;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EventTest {

    @DisplayName("종료일이 시작일보다 빠른 이벤트는 생성할 수 없다")
    @Test
    void cannotCreateEventWithInvalidPeriod() {
        // when // then
        assertThatThrownBy(() -> Event.builder()
                .title("이벤트명")
                .startDate(LocalDate.of(2026, 7, 2))
                .endDate(LocalDate.of(2026, 7, 1))
                .repeatEndDate(LocalDate.of(2026, 7, 1))
                .repeatType(RepeatType.NONE)
                .category(createCategory())
                .build())
                .isInstanceOf(CalendarException.InvalidEventPeriodException.class);
    }

    @DisplayName("종료일이 시작일보다 빠른 기간으로 이벤트를 수정할 수 없다")
    @Test
    void cannotUpdateEventWithInvalidPeriod() {
        // given
        Event event = Event.builder()
                .title("이벤트명")
                .startDate(LocalDate.of(2026, 7, 1))
                .endDate(LocalDate.of(2026, 7, 2))
                .repeatEndDate(LocalDate.of(2026, 7, 2))
                .repeatType(RepeatType.NONE)
                .category(createCategory())
                .build();
        UpdateEventCommand command = new UpdateEventCommand(
                "수정 이벤트명",
                LocalDate.of(2026, 7, 2),
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 1),
                RepeatType.NONE,
                1L
        );

        // when // then
        assertThatThrownBy(() -> event.update(command, createCategory()))
                .isInstanceOf(CalendarException.InvalidEventPeriodException.class);
    }

    @DisplayName("반복 종료일이 이벤트 종료일보다 빠른 이벤트는 생성할 수 없다")
    @Test
    void cannotCreateEventWithInvalidRepeatEndDate() {
        // when // then
        assertThatThrownBy(() -> Event.builder()
                .title("이벤트명")
                .startDate(LocalDate.of(2026, 7, 1))
                .endDate(LocalDate.of(2026, 7, 3))
                .repeatEndDate(LocalDate.of(2026, 7, 2))
                .repeatType(RepeatType.WEEKLY)
                .category(createCategory())
                .build())
                .isInstanceOf(CalendarException.InvalidRepeatEndDateException.class);
    }

    private Category createCategory() {
        return Category.builder()
                .name("활동보고서")
                .color("#FFFFFF")
                .build();
    }
}
