package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery;
import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeListPagingQuery.NoticeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeListResponse(
    @Schema(description = "공지사항 정보")
    List<NoticeDto> notices,

    @Schema(description = "총 페이지 수")
    int totalPage
) {

    public static NoticeListResponse from(NoticeListPagingQuery query) {
        return NoticeListResponse.builder()
            .notices(query.noticeInfos().stream().map(NoticeDto::from).toList())
            .totalPage(query.totalPage())
            .build();
    }

    @Builder
    public record NoticeDto(
        Long id,
        String title,
        LocalDateTime createdAt
    ) {

        public static NoticeDto from(NoticeInfo noticeInfo) {
            return NoticeDto.builder()
                .id(noticeInfo.id())
                .title(noticeInfo.title())
                .createdAt(noticeInfo.createdAt())
                .build();
        }

    }
}
