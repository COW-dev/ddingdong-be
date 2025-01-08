package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record UpdateVodProcessingJobStatusRequest(
        @NotNull(message = "변환 작업 ID는 필수입니다")
        @UUID
        String convertJobId,
        @NotNull(message = "상태는 필수입니다")
        ConvertJobStatus status
) {

    public UpdateVodProcessingJobStatusCommand toCommand() {
        return new UpdateVodProcessingJobStatusCommand(convertJobId, status);
    }

}
