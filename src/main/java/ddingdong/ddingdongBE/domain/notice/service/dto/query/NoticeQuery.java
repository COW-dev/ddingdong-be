package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameWithOrderQuery;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlWithOrderQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeQuery(
        String title,
        String content,
        LocalDateTime createdAt,
        List<UploadedFileUrlWithOrderQuery> images,
        List<UploadedFileUrlAndNameWithOrderQuery> files
) {

    public static NoticeQuery of(
            Notice notice,
            List<UploadedFileUrlWithOrderQuery> images,
            List<UploadedFileUrlAndNameWithOrderQuery> files
    ) {
        return NoticeQuery.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .createdAt(notice.getCreatedAt())
                .images(images)
                .files(files)
                .build();
    }
}
