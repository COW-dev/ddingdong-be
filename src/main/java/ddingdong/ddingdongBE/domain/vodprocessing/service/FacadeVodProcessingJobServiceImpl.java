package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeVodProcessingJobServiceImpl implements FacadeVodProcessingJobService {

    private final VodProcessingJobService vodProcessingJobService;
    private final FileMetaDataService fileMetaDataService;

    @Override
    @Transactional
    public Long create(CreatePendingVodProcessingJobCommand command) {
        FileMetaData fileMetaData = fileMetaDataService.getById(command.fileId());
        return vodProcessingJobService.save(command.toPendingVodProcessingJob(fileMetaData));
    }

    @Override
    @Transactional
    public void updateVodProcessingJobStatus(UpdateVodProcessingJobStatusCommand command) {
        VodProcessingJob vodProcessingJob = vodProcessingJobService.getByConvertJobId(command.convertJobId());
        vodProcessingJob.updateConvertJobStatus(command.status());
    }

}
