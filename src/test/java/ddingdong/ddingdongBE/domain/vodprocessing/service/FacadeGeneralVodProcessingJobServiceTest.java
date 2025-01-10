package ddingdong.ddingdongBE.domain.vodprocessing.service;

import static ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus.COMPLETE;
import static ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodNotificationStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateVodProcessingJobStatusCommand;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeGeneralVodProcessingJobServiceTest extends TestContainerSupport {

    @Autowired
    private FacadeVodProcessingJobService facadeVodProcessingJobService;
    @Autowired
    private VodProcessingJobRepository vodProcessingJobRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @DisplayName("VodProcessingJob 생성: PENDING")
    @Test
    void createPendingVodProcessingJob() {
        //given
        UUID fileId = UuidCreator.getTimeBased();
        FileMetaData fileMetaData = FileMetaData.createPending(fileId, "test", "test");
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(fileMetaData);

        String convertJobId = "testId";
        String userId = "testId";
        CreatePendingVodProcessingJobCommand command = new CreatePendingVodProcessingJobCommand(
                convertJobId, userId, savedFileMetaData.getId().toString());

        //when
        Long createdPendingVodProcessingJobId = facadeVodProcessingJobService.create(command);

        //then
        Optional<VodProcessingJob> result = vodProcessingJobRepository.findById(createdPendingVodProcessingJobId);
        assertThat(result).isPresent();
        assertThat(result.get())
                .satisfies(job -> {
                    assertThat(job.getConvertJobId()).isEqualTo(convertJobId);
                    assertThat(job.getUserId()).isEqualTo(userId);
                    assertThat(job.getConvertJobStatus()).isEqualTo(PENDING);
                    assertThat(job.getVodProcessingNotification())
                            .satisfies(notification ->
                                    assertThat(
                                            notification.getVodNotificationStatus()).isEqualTo(
                                            VodNotificationStatus.PENDING)
                            );
                });
        assertThat(result.get().getFileMetaData().getId()).isEqualTo(fileId);
    }

}
