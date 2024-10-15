package ddingdong.ddingdongBE.domain.banner.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.UpdateBannerRequest;
import ddingdong.ddingdongBE.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update banner set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    //TODO: migration script 작성
    private String webImageKey;

    private String mobileImageKey;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private Banner(Long id, User user, String webImageKey, String mobileImageKey, LocalDateTime deletedAt) {
        this.id = id;
        this.user = user;
        this.webImageKey = webImageKey;
        this.mobileImageKey = mobileImageKey;
        this.deletedAt = deletedAt;
    }

    public void update(Banner banner) {
        this.webImageKey = banner.getWebImageKey();
        this.mobileImageKey = banner.getMobileImageKey();
    }
}
