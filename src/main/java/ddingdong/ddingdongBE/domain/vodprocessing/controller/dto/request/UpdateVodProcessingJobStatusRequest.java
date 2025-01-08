package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record UpdateVodProcessingJobStatusRequest(
        @NotNull
        @UUID
        String convertJobId,
        @NotNull
        ConvertJobStatus status
) {

    public UpdateVodProcessingJobStatusCommand toCommand() {
        return new UpdateVodProcessingJobStatusCommand(convertJobId, status);
    }

}
