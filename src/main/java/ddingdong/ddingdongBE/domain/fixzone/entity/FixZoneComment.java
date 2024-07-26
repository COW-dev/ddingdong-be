package ddingdong.ddingdongBE.domain.fixzone.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import java.util.Objects;
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

@Entity
@Getter
@Table(name = "fix_zone_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
