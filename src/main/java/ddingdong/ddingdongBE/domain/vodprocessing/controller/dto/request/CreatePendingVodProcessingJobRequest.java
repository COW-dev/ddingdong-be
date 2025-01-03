package ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;

public record CreatePendingVodProcessingJobRequest(
        String convertJobId,
        String userId
) {

    public CreatePendingVodProcessingJobCommand toCommand() {
        return new CreatePendingVodProcessingJobCommand(convertJobId, userId);
    }

}
