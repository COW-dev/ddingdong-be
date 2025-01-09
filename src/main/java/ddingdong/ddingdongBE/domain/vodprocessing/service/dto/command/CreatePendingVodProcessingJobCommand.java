package ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;

public record CreatePendingVodProcessingJobCommand(
        String convertJobId,
        String userId
) {

    public VodProcessingJob toPendingVodProcessingJob(VodProcessingNotification notification) {
        return VodProcessingJob.builder()
                .convertJobId(convertJobId)
                .userId(userId)
                .convertJobStatus(ConvertJobStatus.PENDING)
                .vodProcessingNotification(notification)
                .build();
    }

}
