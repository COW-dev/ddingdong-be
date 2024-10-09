package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.service.dto.FileResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeQuery(
    String title,
    String content,
    LocalDateTime createdAt,
    List<String> imageUrls,
    List<FileResponse> fileUrls
) {

    public static NoticeQuery of(Notice notice, List<String> imageUrls, List<FileResponse> fileUrls) {
        return NoticeQuery.builder()
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .imageUrls(imageUrls)
            .fileUrls(fileUrls)
            .build();
    }
}
