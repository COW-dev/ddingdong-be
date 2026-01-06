package ddingdong.ddingdongBE.domain.form.service;

import ddingdong.ddingdongBE.domain.form.entity.FormResultSendingEmailInfo;

public interface FormResultEmailSender {

    void sendResult(FormResultSendingEmailInfo formResultSendingEmailInfo);
}
