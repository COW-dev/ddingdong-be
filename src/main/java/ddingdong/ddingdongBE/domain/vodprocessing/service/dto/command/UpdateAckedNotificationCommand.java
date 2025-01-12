package ddingdong.ddingdongBE.domain.vodprocessing.service.dto.command;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.VodNotificationStatus;

public record UpdateAckedNotificationCommand(
        Long vodNotificationId
) {

}
