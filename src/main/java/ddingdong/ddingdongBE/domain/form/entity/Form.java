package ddingdong.ddingdongBE.domain.form.entity;

import ddingdong.ddingdongBE.common.BaseEntity;
import ddingdong.ddingdongBE.common.converter.StringListConverter;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Form extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private boolean hasInterview;

    @Column(nullable = false)
    @Convert(converter = StringListConverter.class)
    private List<String> sections;

    @ManyToOne(fetch = FetchType.LAZY)
    private Club club;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormField> formFields = new ArrayList<>();

    @Builder
    private Form(
            String title,
            String description,
            LocalDate startDate,
            LocalDate endDate,
            boolean hasInterview,
            List<String> sections,
            Club club,
            List<FormField> formFields
    ) {
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hasInterview = hasInterview;
        this.sections = sections;
        this.club = club;
        this.formFields = formFields;
    }

    public void addFormFields(FormField formField) {
        this.formFields.add(formField);
        formField.setFormForConvenience(this);
    }

    public void update(Form updateForm) {
        this.title = updateForm.getTitle();
        this.description = updateForm.getDescription();
        this.startDate = updateForm.getStartDate();
        this.endDate = updateForm.getEndDate();
        this.sections = updateForm.getSections();
        this.hasInterview = updateForm.isHasInterview();
    }

    public boolean isEqualsById(Long formId) {
        return this.id.equals(formId);
    }

    public void updateFormFields(List<FormField> updatedFormFields) {
        // 삭제될 폼 필드
        List<FormField> deletedFormFields = this.formFields.stream()
                .filter(formField -> updatedFormFields.stream()
                        .filter(updatedFormField -> updatedFormField.getId() != null)
                        .noneMatch(updatedField -> updatedField.getId().equals(formField.getId())))
                .toList();
        this.formFields.removeAll(deletedFormFields);

        Map<Long, FormField> existingFieldMap = this.formFields.stream()
                .collect(Collectors.toMap(FormField::getId, field -> field));

        // 추가 및 업데이트
        for (FormField updatedField : updatedFormFields) {
            if (updatedField.getId() == null || !existingFieldMap.containsKey(updatedField.getId())) {
                // 추가
                addFormFields(updatedField);
            } else {
                // 업데이트
                FormField existingField = existingFieldMap.get(updatedField.getId());
                existingField.update(updatedField);
            }
        }
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isLargerSectionThan(int sectionSize) {
        return this.sections.size() > sectionSize;
    }
}
