package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NoticeListQuery(
    Long id,
    String title,
    LocalDateTime createdAt
) {

    public static NoticeListQuery from(Notice notice) {
        return NoticeListQuery.builder()
            .id(notice.getId())
            .title(notice.getTitle())
            .createdAt(notice.getCreatedAt())
            .build();
    }
}
