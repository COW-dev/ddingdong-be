package ddingdong.ddingdongBE.domain.formapplication.service.dto.command;

import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Builder;

@Builder
public record UpdateFormApplicationNoteCommand (
        Long formId,
        Long applicationId,
        String note,
        User user
){

}
