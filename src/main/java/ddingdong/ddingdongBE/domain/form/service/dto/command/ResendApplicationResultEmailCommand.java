package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;

public record ResendApplicationResultEmailCommand(
        Long userId,
        Long formId,
        FormApplicationStatus target) {

}
