package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.FeedService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import ddingdong.ddingdongBE.sse.service.SseConnectionService;
import ddingdong.ddingdongBE.sse.service.dto.SseEvent;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeVodProcessingJobServiceImpl implements FacadeVodProcessingJobService {

    private final VodProcessingJobService vodProcessingJobService;
    private final FileMetaDataService fileMetaDataService;
    private final VodProcessingNotificationService vodProcessingNotificationService;
    private final SseConnectionService sseConnectionService;
    private final FeedService feedService;

    @Override
    @Transactional
    public Long create(CreatePendingVodProcessingJobCommand command) {
        FileMetaData fileMetaData = fileMetaDataService.getById(command.fileId());
        VodProcessingNotification pendingNotification =
                vodProcessingNotificationService.save(VodProcessingNotification.creatPending());
        return vodProcessingJobService.save(command.toPendingVodProcessingJob(pendingNotification, fileMetaData));
    }

    @Override
    @Transactional
    public void updateVodProcessingJobStatus(UpdateVodProcessingJobStatusCommand command) {
        VodProcessingJob vodProcessingJob = vodProcessingJobService.getByConvertJobId(command.convertJobId());
        vodProcessingJob.updateConvertJobStatus(command.status());
        checkVodProcessingJobStatus(vodProcessingJob);
    }

    private void checkVodProcessingJobStatus(VodProcessingJob vodProcessingJob) {
        if (vodProcessingJob.isPossibleNotify()) {
            checkExistingFeedAndNotify(vodProcessingJob);
        }
    }

    private void checkExistingFeedAndNotify(VodProcessingJob vodProcessingJob) {
        Optional<Feed> optionalFeed = feedService.findById(vodProcessingJob.getFileMetaData().getEntityId());
        if (optionalFeed.isPresent()) {
            SseEvent<ConvertJobStatus> sseEvent = SseEvent.of(
                    "vod-processing",
                    vodProcessingJob.getConvertJobStatus(),
                    LocalDateTime.now()
            );
            sseConnectionService.sendVodProcessingNotification(vodProcessingJob, sseEvent);
        }
    }

}
