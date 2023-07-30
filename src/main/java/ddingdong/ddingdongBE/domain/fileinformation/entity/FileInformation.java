package ddingdong.ddingdongBE.domain.fileinformation.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
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
