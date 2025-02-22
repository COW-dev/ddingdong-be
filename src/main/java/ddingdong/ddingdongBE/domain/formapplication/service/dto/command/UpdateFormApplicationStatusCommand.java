package ddingdong.ddingdongBE.domain.formapplication.service.dto.command;

import ddingdong.ddingdongBE.domain.formapplication.entity.FormApplicationStatus;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record UpdateFormApplicationStatusCommand(
        Long formId,
        List<Long> applicationIds,
        FormApplicationStatus status,
        User user
) {

}
