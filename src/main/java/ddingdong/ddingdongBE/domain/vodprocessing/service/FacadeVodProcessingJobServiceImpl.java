package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeVodProcessingJobServiceImpl implements FacadeVodProcessingJobService {

    private final VodProcessingJobService vodProcessingJobService;

    @Override
    @Transactional
    public Long create(CreatePendingVodProcessingJobCommand command) {
        return vodProcessingJobService.save(command.toPendingVodProcessingJob());
    }
}
