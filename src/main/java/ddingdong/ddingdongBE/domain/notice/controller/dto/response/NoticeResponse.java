package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.file.dto.FileResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private List<String> imageUrls;

    private List<FileResponse> fileUrls;

    public NoticeResponse(String title, String content, LocalDateTime createdAt,
        List<String> imageUrls,
                                List<FileResponse> fileUrls) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
        this.fileUrls = fileUrls;
    }

    public static NoticeResponse of(Notice notice, List<String> imageUrls,
        List<FileResponse> fileUrls) {
        return new NoticeResponse(notice.getTitle(), notice.getContent(), notice.getCreatedAt(),
            imageUrls, fileUrls);
    }

}
