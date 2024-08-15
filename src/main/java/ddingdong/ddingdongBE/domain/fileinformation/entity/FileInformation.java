package ddingdong.ddingdongBE.domain.fileinformation.entity;

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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Table;
import org.hibernate.annotations.Where;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "update file_information set is_deleted = true where id=?")
@Where(clause = "is_deleted = false")
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

    @Column(name = "is_deleted")
    private boolean isDeleted;

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
