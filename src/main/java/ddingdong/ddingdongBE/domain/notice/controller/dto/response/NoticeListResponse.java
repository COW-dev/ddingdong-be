package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record NoticeListResponse(
    @Schema(description = "공지사항 ID", example = "1")
    Long id,
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,
    @Schema(description = "공지사항 생성 날짜 및 시간", example = "2024-01-01 12:12")
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
