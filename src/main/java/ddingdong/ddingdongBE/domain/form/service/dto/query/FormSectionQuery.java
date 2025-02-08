package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import java.util.List;
import lombok.Builder;

@Builder
public record FormSectionQuery(
    String title,
    String description,
    List<String> sections
) {

  public static FormSectionQuery from(Form form) {
    return FormSectionQuery.builder()
        .title(form.getTitle())
        .description(form.getDescription())
        .sections(form.getSections())
        .build();
  }
}
