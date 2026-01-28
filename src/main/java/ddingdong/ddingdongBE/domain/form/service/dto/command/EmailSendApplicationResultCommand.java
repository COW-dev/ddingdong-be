package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;

public record EmailSendApplicationResultCommand(
        Long userId,
        Long formId,
        String title,
        FormApplicationStatus target,
        String message
) {

}
