package ddingdong.ddingdongBE.domain.vodprocessing.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VodProcessingNotification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime expiredAt;

    private LocalDateTime sentAt;

    @Column(nullable = false)
    private int retryCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_status", nullable = false)
    private VodNotificationStatus vodNotificationStatus;

    @Builder
    private VodProcessingNotification(Long id, LocalDateTime expiredAt, LocalDateTime sentAt, int retryCount,
                                     VodNotificationStatus vodNotificationStatus) {
        this.id = id;
        this.expiredAt = expiredAt;
        this.sentAt = sentAt;
        this.retryCount = retryCount;
        this.vodNotificationStatus = vodNotificationStatus;
    }

    public static VodProcessingNotification creatPending() {
        return VodProcessingNotification.builder()
                .retryCount(0)
                .vodNotificationStatus(VodNotificationStatus.PENDING)
                .build();
    }

    public void updateVodNotificationStatusToSent(LocalDateTime sentAt) {
        this.sentAt = sentAt;
        this.vodNotificationStatus = VodNotificationStatus.SENT;
    }

}
