package ddingdong.ddingdongBE.domain.imageinformation.entity;

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
public class ImageInformation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uploadName;

    private String storedName;

    @Enumerated(EnumType.STRING)
    private ImageCategory imageCategory;

    private String findParam;

    @Builder
    public ImageInformation(String uploadName, String storedName, ImageCategory imageCategory, String findParam) {
        this.uploadName = uploadName;
        this.storedName = storedName;
        this.imageCategory = imageCategory;
        this.findParam = findParam;
    }
}
