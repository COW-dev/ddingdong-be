package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeListPagingQuery(
    List<NoticeInfo> noticeInfos,
    int totalPage
) {

    public static NoticeListPagingQuery of(List<NoticeInfo> noticeInfos, int totalPage) {
        return NoticeListPagingQuery.builder()
            .noticeInfos(noticeInfos)
            .totalPage(totalPage)
            .build();
    }

    @Builder
    public record NoticeInfo(
        Long id,
        String title,
        LocalDateTime createdAt
    ) {

        public static NoticeInfo from(Notice notice) {
            return NoticeInfo.builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .build();
        }

    }
}

