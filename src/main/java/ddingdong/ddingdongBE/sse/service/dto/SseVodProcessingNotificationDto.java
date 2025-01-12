package ddingdong.ddingdongBE.sse.service.dto;

import ddingdong.ddingdongBE.domain.vodprocessing.entity.ConvertJobStatus;

public record SseVodProcessingNotificationDto(
        Long id,
        ConvertJobStatus convertJobStatus
) {

}
