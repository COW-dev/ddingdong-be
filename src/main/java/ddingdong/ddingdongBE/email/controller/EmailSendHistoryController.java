package ddingdong.ddingdongBE.email.controller;

import ddingdong.ddingdongBE.email.api.EmailSendHistoryApi;
import ddingdong.ddingdongBE.email.controller.dto.UpdateEmailSendStatusRequest;
import ddingdong.ddingdongBE.email.service.EmailSendHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class EmailSendHistoryController implements EmailSendHistoryApi {

    private final EmailSendHistoryService emailSendHistoryService;

    @Override
    public void updateEmailSendStatus(final UpdateEmailSendStatusRequest request) {
        emailSendHistoryService.updateEmailSendStatus(request.eventType(), request.messageId());
    }
}
