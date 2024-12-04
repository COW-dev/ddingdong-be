package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;

public interface FacadeVodProcessingJobService {

    Long create(CreatePendingVodProcessingJobCommand command);

}
