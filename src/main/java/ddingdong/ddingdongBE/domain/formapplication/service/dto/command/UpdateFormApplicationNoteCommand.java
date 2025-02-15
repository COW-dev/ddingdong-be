package ddingdong.ddingdongBE.domain.formapplication.service.dto.command;

import lombok.Builder;

@Builder
public record UpdateFormApplicationNoteCommand (
        Long applicationId,
        String note
){

}
