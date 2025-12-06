package ddingdong.ddingdongBE.domain.form.service.event;

import ddingdong.ddingdongBE.domain.form.entity.FormResultSendingEmailInfo;
import java.util.List;

public record SendFormResultEvent(
        List<FormResultSendingEmailInfo> formResultSendingEmailInfos
) {

}
