package ddingdong.ddingdongBE.domain.formapplication.service.dto.query;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FormApplicationListQuery(
        Long id,
        Long formId,
        LocalDateTime submittedAt,
        String name,
        String studentNumber,
        String status
) {

    public static FormApplicationListQuery of(FormApplication formApplication) {
        return FormApplicationListQuery.builder()
                .id(formApplication.getId())
                .formId(formApplication.getForm().getId())
                .submittedAt(formApplication.getCreatedAt())
                .name(formApplication.getName())
                .studentNumber(formApplication.getStudentNumber())
                .status(formApplication.getStatus().toString())
                .build();
    }
}
