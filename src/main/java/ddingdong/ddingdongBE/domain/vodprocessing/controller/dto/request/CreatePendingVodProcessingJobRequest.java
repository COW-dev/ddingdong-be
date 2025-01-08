package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.UUID;

public record CreatePendingVodProcessingJobRequest(
        @NotNull
        @UUID
        String convertJobId,
        @NotNull
        String userId
) {

    public CreatePendingVodProcessingJobCommand toCommand() {
        return new CreatePendingVodProcessingJobCommand(convertJobId, userId);
    }

}
