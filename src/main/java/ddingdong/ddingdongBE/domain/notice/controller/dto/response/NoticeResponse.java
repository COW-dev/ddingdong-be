package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeResponse(
    String title,
    String content,
    LocalDateTime createdAt,
    List<String> imageUrls,
    List<FileResponse> fileUrls
) {

    public static NoticeResponse of(Notice notice, List<String> imageUrls, List<FileResponse> fileUrls) {
        return NoticeResponse.builder()
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .imageUrls(imageUrls)
            .fileUrls(fileUrls)
            .build();
    }
}
