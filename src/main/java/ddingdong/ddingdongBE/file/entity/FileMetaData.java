package ddingdong.ddingdongBE.file.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private FileCategory fileCategory;

    @Column(nullable = false)
    private String fileId;

    @Column(nullable = false)
    private String fileName;

    @Builder
    public FileMetaData(Long id, FileCategory fileCategory, String fileId, String fileName) {
        this.id = id;
        this.fileCategory = fileCategory;
        this.fileId = fileId;
        this.fileName = fileName;
    }
}
