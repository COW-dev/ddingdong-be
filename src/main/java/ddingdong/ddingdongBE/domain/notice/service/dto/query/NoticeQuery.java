package ddingdong.ddingdongBE.domain.notice.service.dto.query;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlQuery;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record NoticeQuery(
    String title,
    String content,
    LocalDateTime createdAt,
    List<UploadedFileUrlQuery> imageUrls,
    List<String> fileNames,
    List<UploadedFileUrlQuery> fileUrls
) {

    public static NoticeQuery of(Notice notice, List<UploadedFileUrlQuery> imageUrls,
        List<String> fileNames, List<UploadedFileUrlQuery> fileUrls) {
        return NoticeQuery.builder()
            .title(notice.getTitle())
            .content(notice.getContent())
            .createdAt(notice.getCreatedAt())
            .imageUrls(imageUrls)
            .fileNames(fileNames)
            .fileUrls(fileUrls)
            .build();
    }
}
