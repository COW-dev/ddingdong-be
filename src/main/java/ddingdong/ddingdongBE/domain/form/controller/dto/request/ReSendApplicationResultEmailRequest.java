package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.service.dto.command.ReSendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ReSendApplicationResultEmailRequest(

        @Schema(description = "메일 제목", example = "제목")
        @NotNull(message = "메일 제목은 필수입니다.")
        String title,

        @Schema(
                description = "재전송 대상 상태",
                example = "FIRST_PASS",
                allowableValues = {"FIRST_PASS", "FIRST_FAIL", "FINAL_PASS", "FINAL_FAIL"}
        )
        @NotNull(message = "재전송 대상 상태는 필수입니다.")
        FormApplicationStatus target
) {

    public ReSendApplicationResultEmailCommand toCommand(Long userId, Long formId) {
        return new ReSendApplicationResultEmailCommand(userId, formId, title, target);
    }
}
