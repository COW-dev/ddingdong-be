package ddingdong.ddingdongBE.domain.fixzone.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@SQLDelete(sql = "update fix_zone_comment set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "fix_zone_comment")
public class FixZoneComment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fix_zone_id", nullable = false)
    private FixZone fixZone;

    private String content;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public FixZoneComment(Long id, Club club, FixZone fixZone, String content) {
        this.id = id;
        this.club = club;
        this.fixZone = fixZone;
        this.content = content;
    }

    public void update(Long clubId, String content) {
        if (Objects.equals(clubId, this.club.getId())) {
            this.content = content;
        }
    }

}
