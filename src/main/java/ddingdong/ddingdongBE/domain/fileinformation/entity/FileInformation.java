package ddingdong.ddingdongBE.domain.fileinformation.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@SQLDelete(sql = "update file_information set deleted_at = CURRENT_TIMESTAMP where id=?")
@Where(clause = "deleted_at IS NULL")
@Table(appliesTo = "file_information")
public class FileInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadName;

    private String storedName;

    @Enumerated(EnumType.STRING)
    private FileTypeCategory fileTypeCategory;

    @Enumerated(EnumType.STRING)
    private FileDomainCategory fileDomainCategory;

    private String findParam;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    @Builder
    public FileInformation(String uploadName, String storedName, FileTypeCategory fileTypeCategory,
                           FileDomainCategory fileDomainCategory, String findParam) {
        this.uploadName = uploadName;
        this.storedName = storedName;
        this.fileTypeCategory = fileTypeCategory;
        this.fileDomainCategory = fileDomainCategory;
        this.findParam = findParam;
    }
}
