package ddingdong.ddingdongBE.domain.activityreport.domain;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update activity_report set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class ActivityReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String term;

    @Column(length = 100)
    private String content;

    private String place;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @ElementCollection
    private List<Participant> participants;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @Builder
    public ActivityReport(
        String term,
        String content,
        String place,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<Participant> participants,
        Club club
    ) {
        this.term = term;
        this.content = content;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participants = participants;
        this.club = club;
    }

    public void update(final ActivityReport updatedActivityReport) {
        this.content = updatedActivityReport.getContent();
        this.place = updatedActivityReport.getPlace();
        this.startDate = updatedActivityReport.getStartDate();
        this.endDate = updatedActivityReport.getEndDate();
        this.participants = updatedActivityReport.getParticipants();
    }
}
