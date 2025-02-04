package ddingdong.ddingdongBE.domain.form.service.dto.query;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record FormListQuery(
        Long formId,
        String title,
        LocalDate startDate,
        LocalDate endData,
        boolean isActive
) {

    public static FormListQuery from(Form form, boolean isActive) {
        return FormListQuery.builder()
                .formId(form.getId())
                .title(form.getTitle())
                .startDate(form.getStartDate())
                .endData(form.getEndDate())
                .isActive(isActive)
                .build();
    }

}
