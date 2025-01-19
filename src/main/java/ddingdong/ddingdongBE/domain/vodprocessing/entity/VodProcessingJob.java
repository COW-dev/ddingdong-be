package ddingdong.ddingdongBE.domain.vodprocessing.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VodProcessingJob extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id")
    private VodProcessingNotification vodProcessingNotification;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_meta_data_id")
    private FileMetaData fileMetaData;

    @Column(nullable = false)
    private String convertJobId;

    @Column(nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    private ConvertJobStatus convertJobStatus;

    @Builder
    private VodProcessingJob(Long id, VodProcessingNotification vodProcessingNotification, FileMetaData fileMetaData,
                             String convertJobId, String userId, ConvertJobStatus convertJobStatus) {
        this.id = id;
        this.vodProcessingNotification = vodProcessingNotification;
        this.fileMetaData = fileMetaData;
        this.convertJobId = convertJobId;
        this.userId = userId;
        this.convertJobStatus = convertJobStatus;
    }

    public void updateConvertJobStatus(ConvertJobStatus convertJobStatus) {
        this.convertJobStatus = convertJobStatus;
    }

    public boolean isPossibleNotify() {
        return this.convertJobStatus == ConvertJobStatus.COMPLETE || this.convertJobStatus == ConvertJobStatus.ERROR;
    }

    public boolean isCompleteNotification() {
        return this.vodProcessingNotification.getVodNotificationStatus() == VodNotificationStatus.COMPLETED;
    }
}
