package ddingdong.ddingdongBE.domain.feed.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update feed set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Feed extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String activityContent;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FeedType feedType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    @Column(nullable = false)
    private long viewCount;

    @Column(nullable = false)
    private long likeCount;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    private Feed(Long id, String activityContent, Club club, FeedType feedType, long viewCount, long likeCount) {
        this.id = id;
        this.activityContent = activityContent;
        this.club = club;
        this.feedType = feedType;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
    }

    public boolean isImage() {
        return feedType == FeedType.IMAGE;
    }

    public boolean isVideo() {
        return feedType == FeedType.VIDEO;
    }

    public void update(Feed updateFeed) {
        this.activityContent = updateFeed.getActivityContent();
    }
}
