package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListQuery;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NoticeListResponse(
    Long id,
    String title,
    LocalDateTime createdAt
) {

    public static NoticeListResponse from(NoticeListQuery query) {
        return NoticeListResponse.builder()
            .id(query.id())
            .title(query.title())
            .createdAt(query.createdAt())
            .build();
    }
}
