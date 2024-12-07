package ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;

public record CreatePendingVodProcessingJobCommand(
        String convertJobId,
        String userId
) {

    public VodProcessingJob toPendingVodProcessingJob() {
        return VodProcessingJob.builder()
                .convertJobId(convertJobId)
                .userId(userId)
                .convertJobStatus(ConvertJobStatus.PENDING)
                .build();
    }

}
