package ddingdong.ddingdongBE.domain.vodprocessing.controller;

import ddingdong.ddingdongBE.domain.vodprocessing.api.VodProcessingNotificationApi;
import ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request.AckNotificationRequest;
import ddingdong.ddingdongBE.domain.vodprocessing.service.FacadeVodProcessingNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VodProcessingNotificationController implements VodProcessingNotificationApi {

    private final FacadeVodProcessingNotificationService facadeVodProcessingNotificationService;

    @Override
    public void ackNotification(AckNotificationRequest request) {
        facadeVodProcessingNotificationService.updateAckedNotification(request.toCommand());
    }
}
