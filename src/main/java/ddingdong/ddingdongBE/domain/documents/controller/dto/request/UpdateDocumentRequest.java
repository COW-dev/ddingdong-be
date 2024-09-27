package ddingdong.ddingdongBE.domain.documents.controller.dto.request;

import ddingdong.ddingdongBE.domain.documents.service.dto.command.UpdateDocumentCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Schema(
    name = "UpdateDocumentRequest",
    description = "자료실 자료 수정 요청"
)
@Builder
public record UpdateDocumentRequest(
    @Schema(description = "자료 제목", example = "자료 제목입니다")
    String title
) {

    public UpdateDocumentCommand toCommand() {
        return UpdateDocumentCommand.builder()
            .title(title)
            .build();
    }
}
