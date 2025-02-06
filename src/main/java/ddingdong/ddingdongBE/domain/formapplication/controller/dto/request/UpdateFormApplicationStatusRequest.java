package ddingdong.ddingdongBE.domain.formapplication.controller.dto.request;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationStatusCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UpdateFormApplicationStatusRequest(
        @NotNull(message = "지원자 id 리스트는 필수 입력 사항입니다.")
        @Schema(description = "수정할 지원자 id 리스트", example = "[1, 2, 3]")
        List<Long> applicationIds,

        @NotNull(message = "지원자 상태는 필수 입력 사항입니다.")
        @Schema(description = "수정할 지원자 상태", example = "FIRST_PASS")
        String status
) {
    public UpdateFormApplicationStatusCommand toCommand(Long formId, User user) {
        return UpdateFormApplicationStatusCommand.builder()
                .formId(formId)
                .applicationIds(applicationIds)
                .status(FormApplicationStatus.findStatus(status))
                .user(user)
                .build();
    }
}
