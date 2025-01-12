package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodNotificationStatus;
import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodProcessingNotification;
import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateAckedNotificationCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeVodProcessingNotificationServiceImpl implements FacadeVodProcessingNotificationService {

    private final VodProcessingNotificationService vodProcessingNotificationService;

    @Override
    public void updateAckedNotification(UpdateAckedNotificationCommand command) {
        VodProcessingNotification vodNotification =
                vodProcessingNotificationService.getById(command.vodNotificationId());
        vodNotification.updateVodNotification(VodNotificationStatus.COMPLETED);

    }
}
