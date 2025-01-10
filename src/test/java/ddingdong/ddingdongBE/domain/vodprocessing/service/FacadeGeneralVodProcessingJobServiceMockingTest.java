package ddingdong.ddingdongBE.domain.vodprocessing.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.domain.feed.entity.Feed;
import ddingdong.ddingdongBE.domain.feed.service.FeedService;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.service.FileMetaDataService;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import ddingdong.ddingdongBE.sse.service.SseConnectionService;
import ddingdong.ddingdongBE.sse.service.dto.SseEvent;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FacadeGeneralVodProcessingJobServiceMockingTest{

    @InjectMocks
    private FacadeVodProcessingJobServiceImpl facadeVodProcessingJobService;
    @Mock
    private VodProcessingJobService vodProcessingJobService;
    @Mock
    private FileMetaDataService fileMetaDataService;
    @Mock
    private VodProcessingNotificationService vodProcessingNotificationService;
    @Mock
    private SseConnectionService sseConnectionService;
    @Mock
    private FeedService feedService;

    @DisplayName("작업 완료시 Feed가 존재하면 SSE 이벤트를 발송한다")
    @ParameterizedTest
    @ValueSource(strings = {"COMPLETE", "ERROR"})
    void updateVodProcessingJobStatus_WithExistingFeed_SendsSseEvent(String convertJobStats) {
        // given
        Long userId = 1L;
        Long feedId = 1L;
        String convertJobId = UuidCreator.getTimeBased().toString();

        FileMetaData fileMetaData = FileMetaData.builder()
                .entityId(feedId)
                .build();

        VodProcessingJob vodProcessingJob = VodProcessingJob.builder()
                .convertJobId(convertJobId)
                .convertJobStatus(ConvertJobStatus.PENDING)
                .userId(userId.toString())
                .fileMetaData(fileMetaData)
                .build();

        Feed feed = Feed.builder()
                .id(feedId)
                .build();

        UpdateVodProcessingJobStatusCommand command = new UpdateVodProcessingJobStatusCommand(
                convertJobId,
                ConvertJobStatus.valueOf(convertJobStats)
        );

        when(vodProcessingJobService.getByConvertJobId(convertJobId))
                .thenReturn(vodProcessingJob);
        when(feedService.findById(feedId))
                .thenReturn(Optional.of(feed));

        // when
        facadeVodProcessingJobService.updateVodProcessingJobStatus(command);

        // then
        verify(vodProcessingJobService).getByConvertJobId(convertJobId);
        verify(feedService).findById(feedId);
        verify(sseConnectionService).send(eq(userId.toString()), any(SseEvent.class));
    }

    @DisplayName("작업 완료시 Feed가 존재하지 않으면 SSE 이벤트를 발송하지 않는다")
    @Test
    void updateVodProcessingJobStatus_WithoutFeed_DoesNotSendSseEvent() {
        // given
        Long userId = 1L;
        Long feedId = 1L;
        String convertJobId = UuidCreator.getTimeBased().toString();

        FileMetaData fileMetaData = FileMetaData.builder()
                .entityId(feedId)
                .build();

        VodProcessingJob vodProcessingJob = VodProcessingJob.builder()
                .convertJobId(convertJobId)
                .convertJobStatus(ConvertJobStatus.PENDING)
                .userId(userId.toString())
                .fileMetaData(fileMetaData)
                .build();

        UpdateVodProcessingJobStatusCommand command = new UpdateVodProcessingJobStatusCommand(
                convertJobId,
                ConvertJobStatus.COMPLETE
        );

        when(vodProcessingJobService.getByConvertJobId(convertJobId))
                .thenReturn(vodProcessingJob);
        when(feedService.findById(feedId))
                .thenReturn(Optional.empty());

        // when
        facadeVodProcessingJobService.updateVodProcessingJobStatus(command);

        // then
        verify(vodProcessingJobService).getByConvertJobId(convertJobId);
        verify(feedService).findById(feedId);
        verify(sseConnectionService, never()).send(any(), any());
    }


}
