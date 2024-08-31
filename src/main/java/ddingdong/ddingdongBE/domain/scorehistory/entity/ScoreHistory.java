package ddingdong.ddingdongBE.domain.scorehistory.entity;

import ddingdong.ddingdongBE.common.BaseEntity;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update score_history set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(name = "score_history")
public class ScoreHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private ScoreCategory scoreCategory;

    private String reason;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public ScoreHistory(Club club, BigDecimal amount, ScoreCategory scoreCategory, String reason) {
        this.club = club;
        this.amount = amount;
        this.scoreCategory = scoreCategory;
        this.reason = reason;
    }
}
