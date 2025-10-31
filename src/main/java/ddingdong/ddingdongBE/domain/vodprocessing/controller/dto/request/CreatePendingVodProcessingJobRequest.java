package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreatePendingVodProcessingJobRequest(
        @NotNull(message = "변환 작업 ID는 필수입니다")
        String convertJobId,
        @NotNull(message = "userId는 필수입니다.")
        String userId,

        @NotNull(message = "파일Id는 필수입니다.")
        @Pattern(regexp = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-7[0-9A-Fa-f]{3}-[89ab][0-9A-Fa-f]{3}-[0-9A-Fa-f]{12}$",
                message = "파일Id는 UUID v7 형식이어야 합니다.")
        String fileId
) {
    public CreatePendingVodProcessingJobCommand toCommand() {
        return new CreatePendingVodProcessingJobCommand(convertJobId, userId, fileId);
    }
}
