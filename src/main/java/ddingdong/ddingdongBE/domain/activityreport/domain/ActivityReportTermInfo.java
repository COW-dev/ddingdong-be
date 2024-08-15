package ddingdong.ddingdongBE.domain.activityreport.domain;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update activity_report_term_info set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
@Table(appliesTo = "activity_report_term_info")
public class ActivityReportTermInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int term;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate endDate;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @Builder
    public ActivityReportTermInfo(int term, LocalDate startDate, LocalDate endDate) {
        this.term = term;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
