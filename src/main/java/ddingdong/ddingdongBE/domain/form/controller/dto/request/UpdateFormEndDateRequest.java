package ddingdong.ddingdongBE.domain.form.controller.dto.request;

import ddingdong.ddingdongBE.domain.form.service.dto.command.UpdateFormEndDateCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

public record UpdateFormEndDateRequest (
        @Schema(description = "폼지 종료일자", example = "2025-03-10")
        LocalDate endDate
){
    public UpdateFormEndDateCommand toCommand(User user, Long formId) {
        return UpdateFormEndDateCommand.builder()
                .user(user)
                .formId(formId)
                .endDate(endDate)
                .build();
    }
}
