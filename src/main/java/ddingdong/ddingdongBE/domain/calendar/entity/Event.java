package ddingdong.ddingdongBE.domain.calendar.entity;

import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateEventCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RepeatType repeatType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @Builder
    private Event(
            String title,
            LocalDate startDate,
            LocalDate endDate,
            RepeatType repeatType,
            Category category
    ) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.repeatType = repeatType;
        this.category = category;
    }

    public void update(UpdateEventCommand command, Category category) {
        this.title = command.title();
        this.startDate = command.startDate();
        this.endDate = command.endDate();
        this.repeatType = command.repeatType();
        this.category = category;
    }

}