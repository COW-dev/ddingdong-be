package ddingdong.ddingdongBE.domain.filemetadata.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FileMetaData extends BaseEntity {

    @Id
    @Column(length = 16)
    private UUID fileId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FileCategory fileCategory;

    @Column(nullable = false)
    private String fileName;

    @Builder
    public FileMetaData(FileCategory fileCategory, UUID fileId, String fileName) {
        this.fileId = fileId;
        this.fileCategory = fileCategory;
        this.fileName = fileName;
    }
}
