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
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FormField extends BaseEntity {
    private static final String COMMON_SECTION = "공통";
    private static final int SINGLE_SECTION_SIZE = 1;

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

    public Stream<FormField> generateFormFieldsBySection(Form form) {
        if (form.isLargerSectionThan(SINGLE_SECTION_SIZE)) {
            return expandCommonSectionFormField(form);
        }
        return Stream.of(this);
    }

    private Stream<FormField> expandCommonSectionFormField(Form form) {
        if (this.section.equals(COMMON_SECTION)) {
            return form.getSections().stream()
                    .filter(section -> !section.equals(COMMON_SECTION))
                    .map(this::copyWithSection);
        }
        return Stream.of(this);
    }

    private FormField copyWithSection(String newSection) {
        return FormField.builder()
                .question(this.question)
                .fieldType(this.fieldType)
                .required(this.required)
                .fieldOrder(this.fieldOrder)
                .section(newSection)
                .options(this.options)
                .form(this.form)
                .build();
    }
}
