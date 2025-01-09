package ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;

public record CreatePendingVodProcessingJobCommand(
        String convertJobId,
        String userId,
        String fileId
) {

    public VodProcessingJob toPendingVodProcessingJob(FileMetaData fileMetaData) {
        return VodProcessingJob.builder()
                .convertJobId(convertJobId)
                .fileMetaData(fileMetaData)
                .userId(userId)
                .convertJobStatus(ConvertJobStatus.PENDING)
                .build();
    }

}
