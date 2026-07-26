package ddingdong.ddingdongBE.domain.calendar.entity;

import ddingdong.ddingdongBE.domain.calendar.service.dto.command.UpdateCategoryCommand;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update category set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(UpdateCategoryCommand command) {
        this.name = command.name();
        this.color = command.color();
    }
}
