package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodNotificationStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import ddingdong.ddingdongBE.domain.vodprocessing.repository.VodProcessingNotificationRepository;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateAckedNotificationCommand;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeVodProcessingNotificationServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeVodProcessingNotificationService facadeVodProcessingNotificationService;
    @Autowired
    private VodProcessingNotificationRepository vodProcessingNotificationRepository;

    @DisplayName("VodProcessingNotification 전송 상태를 전송 완료로 변경한다.")
    @Test
    void update() {
        //given
        VodProcessingNotification savedVodProcessingNotification = vodProcessingNotificationRepository.save(
                VodProcessingNotification.creatPending());

        UpdateAckedNotificationCommand command = new UpdateAckedNotificationCommand(
                savedVodProcessingNotification.getId());

        //when
        facadeVodProcessingNotificationService.updateAckedNotification(command);

        //then
        Optional<VodProcessingNotification> result = vodProcessingNotificationRepository.findById(
                savedVodProcessingNotification.getId());
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get().getVodNotificationStatus()).isEqualTo(VodNotificationStatus.COMPLETED);
    }

}
