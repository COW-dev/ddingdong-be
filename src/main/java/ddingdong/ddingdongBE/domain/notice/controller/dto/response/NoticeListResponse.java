package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NoticeListResponse(
    Long id,
    String title,
    LocalDateTime createdAt
) {

    public static NoticeListResponse from(Notice notice) {
        return NoticeListResponse.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .createdAt(notice.getCreatedAt())
            .build();
    }
}
