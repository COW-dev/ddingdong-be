package ddingdong.ddingdongBE.domain.scorehistory.entity;

import ddingdong.ddingdongBE.common.BaseEntity;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ScoreHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    private int amount;

    @Enumerated(EnumType.STRING)
    private ScoreCategory scoreCategory;

    private String reason;

    private int remainingScore;

    @Builder
    public ScoreHistory(Club club, int amount, ScoreCategory scoreCategory, String reason, int remainingScore) {
        this.club = club;
        this.amount = amount;
        this.scoreCategory = scoreCategory;
        this.reason = reason;
        this.remainingScore = remainingScore;
    }
}
