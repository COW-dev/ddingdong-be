package ddingdong.ddingdongBE.domain.feed.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "target_year", "target_month"}))
public class FeedMonthlyRanking extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long clubId;

    @Column(nullable = false)
    private String clubName;

    @Column(nullable = false)
    private long feedCount;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long likeCount;

    @Column(nullable = false)
    private long commentCount;

    @Column(nullable = false)
    private long score;

    @Column(nullable = false)
    private int targetYear;

    @Column(nullable = false)
    private int targetMonth;

    @Builder
    private FeedMonthlyRanking(Long id, Long clubId, String clubName, long feedCount,
            long viewCount, long likeCount, long commentCount, long score,
            int targetYear, int targetMonth) {
        this.id = id;
        this.clubId = clubId;
        this.clubName = clubName;
        this.feedCount = feedCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.score = score;
        this.targetYear = targetYear;
        this.targetMonth = targetMonth;
    }
}
