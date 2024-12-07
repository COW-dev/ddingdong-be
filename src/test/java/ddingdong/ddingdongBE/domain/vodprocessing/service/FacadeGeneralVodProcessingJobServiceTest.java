package ddingdong.ddingdongBE.domain.vodprocessing.service;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingJob;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingJobRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.CreatePendingVodProcessingJobCommand;
import java.util.Optional;
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

    @DisplayName("VodProcessingJob 생성: PENDING")
    @Test
    void createPendingVodProcessingJob() {
        //given
        String convertJobId = "testId";
        String userId = "testId";
        CreatePendingVodProcessingJobCommand command = new CreatePendingVodProcessingJobCommand(
                convertJobId, userId);

        //when
        Long createdPendingVodProcessingJobId = facadeVodProcessingJobService.create(command);

        //then
        Optional<VodProcessingJob> result = vodProcessingJobRepository.findById(createdPendingVodProcessingJobId);
        assertThat(result).isPresent();
        assertThat(result.get())
                .extracting(VodProcessingJob::getConvertJobId, VodProcessingJob::getUserId,
                        VodProcessingJob::getConvertJobStatus)
                .containsExactly(convertJobId, userId, ConvertJobStatus.PENDING);
    }

}
