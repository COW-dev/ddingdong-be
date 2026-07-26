package ddingdong.ddingdongBE.domain.calendar.entity;

import ddingdong.ddingdongBE.common.exception.CalendarException;
import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update event set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private LocalDate repeatEndDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatType repeatType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Event(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            LocalDate repeatEndDate,
            RepeatType repeatType,
            Category category
    ) {
        validatePeriod(startDate, endDate);
        validateRepeatEndDate(endDate, repeatEndDate);
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatEndDate = repeatEndDate;
        this.repeatType = repeatType;
        this.category = category;
    }

    public void update(UpdateEventCommand command, Category category) {
        validatePeriod(command.startDate(), command.endDate());
        validateRepeatEndDate(command.endDate(), command.repeatEndDate());
        this.title = command.title();
        this.startDate = command.startDate();
        this.endDate = command.endDate();
        this.repeatEndDate = command.repeatEndDate();
        this.repeatType = command.repeatType();
        this.category = category;
    }

    private void validatePeriod(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new CalendarException.InvalidEventPeriodException();
        }
    }

    private void validateRepeatEndDate(LocalDate endDate, LocalDate repeatEndDate) {
        if (repeatEndDate.isBefore(endDate)) {
            throw new CalendarException.InvalidRepeatEndDateException();
        }
    }

}
