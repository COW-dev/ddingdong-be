package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record CreatePendingVodProcessingJobRequest(
        @NotNull(message = "변환 작업 ID는 필수입니다")
        @UUID
        String convertJobId,
        @NotNull(message = "userId는 필수입니다.")
        String userId,
        @NotNull(message = "파일Id는 빌수입니다.")
        @UUID
        String fileId
) {

    public CreatePendingVodProcessingJobCommand toCommand() {
        return new CreatePendingVodProcessingJobCommand(convertJobId, userId, fileId);
    }

}
