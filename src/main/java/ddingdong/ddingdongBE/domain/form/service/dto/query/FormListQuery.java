package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.form.entity.FormStatus;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FormListQuery(
    Long formId,
    String title,
    LocalDate startDate,
    LocalDate endDate,
    String formStatus
) {

  public static FormListQuery from(Form form, FormStatus formStatus) {
    return FormListQuery.builder()
        .formId(form.getId())
        .title(form.getTitle())
        .startDate(form.getStartDate())
        .endDate(form.getEndDate())
        .formStatus(formStatus.getDescription())
        .build();
  }

}
