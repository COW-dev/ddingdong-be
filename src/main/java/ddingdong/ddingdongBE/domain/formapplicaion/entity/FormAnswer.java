package ddingdong.ddingdongBE.domain.formapplicaion.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FormAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Convert(converter = StringListConverter.class)
    private List<String> value;

    @JoinColumn(name = "application_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FormApplication formApplication;

    @JoinColumn(name = "field_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private FormField formField;

    @Builder
    private FormAnswer(List<String> value, FormApplication formApplication, FormField formField) {
        this.value = value;
        this.formApplication = formApplication;
        this.formField = formField;
    }

}
