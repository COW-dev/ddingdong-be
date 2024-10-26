package ddingdong.ddingdongBE.domain.notice.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import ddingdong.ddingdongBE.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@SQLDelete(sql = "update notice set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    @Convert(converter = StringListConverter.class)
    @Column(name = "notice_image_keys")
    private List<String> imageKeys;

    @Column(columnDefinition = "json")
    private String fileInfos;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    public Notice(User user, String title, String content, List<String> imageKeys,
        String fileInfos) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.imageKeys = imageKeys;
        this.fileInfos = fileInfos;
    }

    public void update(Notice notice) {
        this.title = notice.getTitle();
        this.content = notice.getContent();

        if (checkKeyExists(notice.imageKeys)) {
            this.imageKeys = notice.imageKeys;
        }

        if (checkFileInfoExists(notice.fileInfos)) {
            this.fileInfos = notice.fileInfos;
        }
    }

    private boolean checkKeyExists(List<String> keys) {
        return keys != null && !keys.isEmpty();
    }

    private boolean checkFileInfoExists(String fileInfos) {
        return fileInfos != null && !fileInfos.isEmpty();
    }

}
