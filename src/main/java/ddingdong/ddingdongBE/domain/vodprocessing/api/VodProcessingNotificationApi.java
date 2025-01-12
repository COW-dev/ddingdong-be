package ddingdong.ddingdongBE.domain.vodprocessing.api;

import ddingdong.ddingdongBE.domain.vodprocessing.controller.dto.request.AckNotificationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "VodProcessingNotification", description = "VodProcessing Noticiation API")
@RequestMapping("/server/vod-processing-notification")
public interface VodProcessingNotificationApi {

    @Operation(
            summary = "SSE 알림 ack"
    )
    @ApiResponse(responseCode = "200", description = "SSE 구독 연결 성공")
    @PostMapping("/ack")
    void ackNotification(@RequestBody AckNotificationRequest request);

}
