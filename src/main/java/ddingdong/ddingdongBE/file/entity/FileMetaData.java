package ddingdong.ddingdongBE.file.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
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
