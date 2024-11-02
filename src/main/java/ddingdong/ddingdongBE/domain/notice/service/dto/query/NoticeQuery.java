package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeQuery(
    String title,
    String content,
    LocalDateTime createdAt,
    List<UploadedFileUrlQuery> images,
    List<UploadedFileUrlAndNameQuery> files
) {

    public static NoticeQuery of(Notice notice, List<UploadedFileUrlQuery> images,
        List<UploadedFileUrlAndNameQuery> files) {
        return NoticeQuery.builder()
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .images(images)
            .files(files)
            .build();
    }
}
