package ddingdong.ddingdongBE.domain.formapplication.controller.dto.request;

import ddingdong.ddingdongBE.domain.formapplication.service.dto.command.UpdateFormApplicationNoteCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateFormApplicationNoteRequest(
        @NotNull(message = "수정할 메모는 필수 입력 사항입니다.")
        @Schema(description = "추가할 메모", example = "좋은 답변이었습니다.")
        String note
) {
    public UpdateFormApplicationNoteCommand toCommand(Long applicationId) {
        return UpdateFormApplicationNoteCommand.builder()
                .applicationId(applicationId)
                .note(note)
                .build();
    }
}
