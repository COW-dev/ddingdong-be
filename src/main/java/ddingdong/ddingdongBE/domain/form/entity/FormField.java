package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update form_field set deleted_at = CURRENT_TIMESTAMP where id=?")
@SQLRestriction("deleted_at IS NULL")
public class FormField extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private int fieldOrder;

    @Column(nullable = false)
    private String section;

    @Convert(converter = StringListConverter.class)
    private List<String> options;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FieldType fieldType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Form form;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    @Builder
    private FormField(
            Long id,
            String question,
            FieldType fieldType,
            boolean required,
            int fieldOrder,
            String section,
            List<String> options,
            Form form
    ) {
        this.id = id;
        this.question = question;
        this.fieldType = fieldType;
        this.required = required;
        this.fieldOrder = fieldOrder;
        this.section = section;
        this.options = options;
        this.form = form;
    }

    public boolean isMultipleChoice() {
        return this.fieldType == FieldType.CHECK_BOX || this.fieldType == FieldType.RADIO;
    }

    public boolean isTextType() {
        return this.fieldType == FieldType.TEXT || this.fieldType == FieldType.LONG_TEXT
                || this.fieldType == FieldType.FILE;
    }

    public boolean isFile() {
        return this.fieldType == FieldType.FILE;
    }

    public void setFormForConvenience(Form form) {
        this.form = form;
    }

    public void update(FormField updatedField) {
        this.question = updatedField.getQuestion();
        this.fieldType = updatedField.getFieldType();
        this.required = updatedField.isRequired();
        this.fieldOrder = updatedField.getFieldOrder();
        this.section = updatedField.getSection();
        this.options = updatedField.getOptions();
    }
}
