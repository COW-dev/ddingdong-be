package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record GetAllNoticeByPageResponse(
    List<NoticeResponseDto> notices
) {

    public static GetAllNoticeByPageResponse from(List<Notice> notices) {

        return GetAllNoticeByPageResponse
            .builder()
            .notices(
                notices.stream()
                    .map(NoticeResponseDto::from)
                    .toList()
            )
            .build();
    }

    @Builder
    public record NoticeResponseDto(
        Long id,
        String title,
        LocalDateTime createdAt
    ) {

        public static NoticeResponseDto from(Notice notice) {
            return NoticeResponseDto
                .builder()
                .id(notice.getId())
                .title(notice.getTitle())
                .createdAt(notice.getCreatedAt())
                .build();
        }

    }

}

