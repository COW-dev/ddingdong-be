package ddingdong.ddingdongBE.domain.banner.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.banner.controller.dto.request.UpdateBannerRequest;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update banner set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(appliesTo = "banner")
public class Banner extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String subTitle;

    private String colorCode;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Banner(User user, String title, String subTitle, String colorCode) {
        this.user = user;
        this.title = title;
        this.subTitle = subTitle;
        this.colorCode = colorCode;
    }

    public void update(UpdateBannerRequest request) {
        this.title = request.getTitle();
        this.subTitle = request.getSubTitle();
        this.colorCode = request.getColorCode();
    }
}
