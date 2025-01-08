package ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;

public record UpdateVodProcessingJobStatusCommand(
        String convertJobId,
        ConvertJobStatus convertJobStatus
) {

}
