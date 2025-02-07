package ddingdong.ddingdongBE.domain.form.service.dto.query;

import java.util.List;
import lombok.Builder;

@Builder
public record FormSectionQuery (
    String title,
    String description,
    List<String> sections
){
  public static FormSectionQuery of(String title, String description, List<String> sections) {
    return FormSectionQuery.builder()
        .title(title)
        .description(description)
        .sections(sections)
        .build();
  }
}
