package ddingdong.ddingdongBE.domain.vodprocessing.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//TODO: migration script
public class VodProcessingJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private VodProcessingNotification vodProcessingNotification;

    @Column(nullable = false)
    private String convertJobId;

    @Column(nullable = false)
    private String userAuthId;

    @Enumerated(EnumType.STRING)
    private ConvertJobStatus convertJobStatus;

    private VodProcessingJob(Long id, VodProcessingNotification vodProcessingNotification, String convertJobId,
                             String userAuthId, ConvertJobStatus convertJobStatus) {
        this.id = id;
        this.vodProcessingNotification = vodProcessingNotification;
        this.convertJobId = convertJobId;
        this.userAuthId = userAuthId;
        this.convertJobStatus = convertJobStatus;
    }
}
