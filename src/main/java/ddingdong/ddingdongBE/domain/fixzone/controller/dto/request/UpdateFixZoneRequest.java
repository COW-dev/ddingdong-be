package ddingdong.ddingdongBE.domain.fixzone.controller.dto.request;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateFixZoneRequest(
        @NotNull
        @Schema(description = "제목")
        String title,
        @NotNull
        @Schema(description = "내용")
        String content,
        @Schema(description = "픽스존 이미지 식별자 목록", example = "[\"0192c828-ffce-7ee8-94a8-d9d4c8cdec00\", \"0192c828-ffce-7ee8-94a8-d9d4c8cdec00\"]")
        List<String> fixZoneImageIds
) {

    public UpdateFixZoneCommand toCommand(Long fixZoneId) {
        return new UpdateFixZoneCommand(
                fixZoneId,
                title,
                content,
                fixZoneImageIds
        );
    }

}
