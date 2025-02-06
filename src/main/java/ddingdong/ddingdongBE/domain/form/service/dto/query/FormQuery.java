package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

@Builder
public record FormQuery(
        String title,
        String description,
        LocalDate startDate,
        LocalDate endDate,
        boolean hasInterview,
        List<String> sections,
        List<FormFieldListQuery> formFields
) {

    @Builder
    public record FormFieldListQuery(
            String question,
            FieldType type,
            List<String> options,
            boolean required,
            int order,
            String section
    ) {
        public static FormFieldListQuery from(FormField formField) {
            return FormFieldListQuery.builder()
                    .question(formField.getQuestion())
                    .type(formField.getFieldType())
                    .options(formField.getOptions())
                    .required(formField.isRequired())
                    .order(formField.getFieldOrder())
                    .section(formField.getSection())
                    .build();
        }
    }

    public static FormQuery of(Form form, List<FormField> formFields) {
        List<FormFieldListQuery> formFieldListQueries = formFields.stream()
                .map(FormFieldListQuery::from)
                .toList();

        return FormQuery.builder()
                .title(form.getTitle())
                .description(form.getDescription())
                .startDate(form.getStartDate())
                .endDate(form.getEndDate())
                .hasInterview(form.isHasInterview())
                .sections(form.getSections())
                .formFields(formFieldListQueries)
                .build();
    }
}
