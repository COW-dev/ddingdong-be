package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.FieldType;
import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormField;
import java.util.List;
import lombok.Builder;

@Builder
public record UserFormQuery (
    String title,
    String description,
    List<UserFormFieldListQuery> formFields
) {

  public static UserFormQuery from(Form form, List<FormField> formFields) {
    List<UserFormFieldListQuery> formFieldListQueries = formFields.stream()
        .map(UserFormFieldListQuery::from)
        .toList();

    return UserFormQuery.builder()
        .title(form.getTitle())
        .description(form.getDescription())
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
