package ddingdong.ddingdongBE.domain.form.service.dto.command;

import ddingdong.ddingdongBE.domain.user.entity.User;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record UpdateFormEndDateCommand (
        Long formId,
        LocalDate endDate,
        User user
){

}
