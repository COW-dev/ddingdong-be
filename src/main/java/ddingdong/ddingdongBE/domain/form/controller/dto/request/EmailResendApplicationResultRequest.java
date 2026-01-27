package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.service.dto.command.EmailResendApplicationResultCommand;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record EmailResendApplicationResultRequest(

        @Schema(
                description = "재전송 대상 상태",
                example = "FIRST_PASS",
                allowableValues = {"FIRST_PASS", "FIRST_FAIL", "FINAL_PASS", "FINAL_FAIL"}
        )
        @NotNull(message = "재전송 대상 상태는 필수입니다.")
        FormApplicationStatus target
) {

    public EmailResendApplicationResultCommand toCommand(Long userId, Long formId) {
        return new EmailResendApplicationResultCommand(userId, formId, target);
    }
}
