package ddingdong.ddingdongBE.domain.filemetadata.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file_meta_data",indexes = {@Index(columnList = "domainType,entityId,fileStatus")})
@SQLDelete(sql = "update file_meta_data set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class FileMetaData extends BaseEntity {

    @Id
    @Column(length = 16)
    private UUID id;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    private DomainType domainType;

    private Long entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileStatus fileStatus;

    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FileMetaData(UUID id, String fileKey, String fileName, DomainType domainType, Long entityId,
                         FileStatus fileStatus,
                         FileCategory fileCategory) {
        this.id = id;
        this.fileKey = fileKey;
        this.fileName = fileName;
        this.domainType = domainType;
        this.entityId = entityId;
        this.fileStatus = fileStatus;
        this.fileCategory = fileCategory;
    }

    public static FileMetaData createPending(UUID id, String fileKey, String fileName) {
        return FileMetaData.builder()
                .id(id)
                .fileKey(fileKey)
                .fileName(fileName)
                .fileStatus(FileStatus.PENDING)
                .build();
    }

    public void updateStatus(FileStatus fileStatus) {
        this.fileStatus = fileStatus;
    }

    public void updateCoupledEntityInfo(DomainType domainType, Long entityId) {
        this.domainType = domainType;
        this.entityId = entityId;
    }

}
