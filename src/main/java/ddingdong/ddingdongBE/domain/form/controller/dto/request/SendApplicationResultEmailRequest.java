package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.service.dto.command.SendApplicationResultEmailCommand;
import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SendApplicationResultEmailRequest(

        @Schema(description = "메일 제목", example = "제목")
        @NotNull(message = "메일 제목은 필수입니다.")
        String title,

        @Schema(description = "전송 대상",
                example = "FIRST_PASS",
                allowableValues = {"FIRST_PASS", "FIRST_FAIL", "FINAL_PASS", "FINAL_FAIL"}
        )
        @NotNull(message = "전송 대상은 필수입니다.")
        FormApplicationStatus target,

        @Schema(description = "내용", example = "내용")
        @NotNull(message = "메일 내용은 필수입니다.")
        String message
) {

    public SendApplicationResultEmailCommand toCommand(Long userId, Long formId) {
        return new SendApplicationResultEmailCommand(userId, formId, title, target, message);
    }


}
