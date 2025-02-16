package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.util.List;
import lombok.Builder;

@Builder
public record UserFormQuery(
        String clubName,
        String title,
        String description,
        int applicationCount,
        List<UserFormFieldListQuery> formFields
) {

    public static UserFormQuery from(Club club, Form form, int applicationCount, List<FormField> formFields) {
        List<UserFormFieldListQuery> formFieldListQueries = formFields.stream()
                .map(UserFormFieldListQuery::from)
                .toList();

        return UserFormQuery.builder()
                .clubName(club.getName())
                .title(form.getTitle())
                .description(form.getDescription())
                .applicationCount(applicationCount)
                .formFields(formFieldListQueries)
                .build();
    }

    @Builder
    public record UserFormFieldListQuery(
            Long id,
            String question,
            FieldType type,
            List<String> options,
            boolean required,
            int order,
            String section
    ) {

        public static UserFormFieldListQuery from(FormField formField) {
            return UserFormFieldListQuery.builder()
                    .id(formField.getId())
                    .question(formField.getQuestion())
                    .type(formField.getFieldType())
                    .options(formField.getOptions())
                    .required(formField.isRequired())
                    .order(formField.getFieldOrder())
                    .section(formField.getSection())
                    .build();
        }
    }
}
