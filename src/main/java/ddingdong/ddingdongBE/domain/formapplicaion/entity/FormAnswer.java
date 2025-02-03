package ddingdong.ddingdongBE.domain.formapplicaion.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FormAnswer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> value;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType valueType;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormApplication formApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    private FormField formField;

    @Builder
    private FormAnswer(List<String> value, FieldType valueType, FormApplication formApplication, FormField formField) {
        this.value = value;
        this.valueType = valueType;
        this.formApplication = formApplication;
        this.formField = formField;
    }

}
