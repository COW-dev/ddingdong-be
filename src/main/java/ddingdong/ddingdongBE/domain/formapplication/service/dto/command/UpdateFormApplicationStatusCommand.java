package ddingdong.ddingdongBE.domain.formapplication.service.dto.command;

import ddingdong.ddingdongBE.domain.form.entity.Form;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplication;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UpdateFormApplicationStatusCommand(
        Long formId,
        Long applicationId,
        FormApplicationStatus status,
        User user
) {
    public FormApplication toEntity(Form form) {
        return FormApplication.builder()
                .form(form)
                .status(status)
                .build();
    }
}
