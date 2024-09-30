package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.service.dto.query.NoticeQuery;
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

    public static NoticeResponse from(NoticeQuery query) {
        return NoticeResponse.builder()
            .title(query.title())
            .content(query.content())
            .createdAt(query.createdAt())
            .imageUrls(query.imageUrls())
            .fileUrls(query.fileUrls())
            .build();
    }
}
