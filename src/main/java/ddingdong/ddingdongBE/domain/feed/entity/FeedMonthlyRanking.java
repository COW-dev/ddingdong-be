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

// 스냅샷 이력 테이블이므로 soft delete를 적용하지 않는다
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"club_id", "target_year", "target_month"}))
public class FeedMonthlyRanking extends BaseEntity {

    private static final int FEED_WEIGHT = 10;
    private static final int VIEW_WEIGHT = 1;
    private static final int LIKE_WEIGHT = 3;
    private static final int COMMENT_WEIGHT = 5;

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
    private int ranking;

    @Column(nullable = false)
    private int targetYear;

    @Column(nullable = false)
    private int targetMonth;

    @Builder
    private FeedMonthlyRanking(Long id, Long clubId, String clubName, long feedCount,
            long viewCount, long likeCount, long commentCount,
            int targetYear, int targetMonth) {
        this.id = id;
        this.clubId = clubId;
        this.clubName = clubName;
        this.feedCount = feedCount;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.targetYear = targetYear;
        this.targetMonth = targetMonth;
        this.score = calculateScore();
    }

    public long calculateScore() {
        return feedCount * FEED_WEIGHT
                + viewCount * VIEW_WEIGHT
                + likeCount * LIKE_WEIGHT
                + commentCount * COMMENT_WEIGHT;
    }

    public void assignRanking(int ranking) {
        this.ranking = ranking;
    }
}
