package ddingdong.ddingdongBE.domain.vodprocessing.service;

import ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command.UpdateAckedNotificationCommand;

public interface FacadeVodProcessingNotificationService {

    void updateAckedNotification(UpdateAckedNotificationCommand command);

}
