package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Form extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean hasInterview;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    @Builder
    private Form(
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            boolean hasInterview,
            Club club
    ) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasInterview = hasInterview;
        this.club = club;
    }

    public void update(Form updateForm) {
        this.title = updateForm.getTitle();
        this.description = updateForm.getDescription();
        this.startDate = updateForm.getStartDate();
        this.endDate = updateForm.getEndDate();
        this.hasInterview = updateForm.isHasInterview();
    }
}
