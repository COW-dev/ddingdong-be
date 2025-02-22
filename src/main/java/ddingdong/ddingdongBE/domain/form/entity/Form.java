package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.List;
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

    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> sections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    @Builder
    private Form(
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            boolean hasInterview,
            List<String> sections,
            Club club
    ) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasInterview = hasInterview;
        this.sections = sections;
        this.club = club;
    }

    public void update(Form updateForm) {
        this.title = updateForm.getTitle();
        this.description = updateForm.getDescription();
        this.startDate = updateForm.getStartDate();
        this.endDate = updateForm.getEndDate();
        this.sections = updateForm.getSections();
        this.hasInterview = updateForm.isHasInterview();
    }

    public boolean isEqualsById(Long formId) {
        return this.id.equals(formId);
    }
}
