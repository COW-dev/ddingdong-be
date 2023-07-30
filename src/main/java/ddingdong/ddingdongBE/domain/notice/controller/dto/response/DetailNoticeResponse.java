package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailNoticeResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private List<String> imageUrls;

    private List<String> fileUrls;

    public DetailNoticeResponse(String title, String content, LocalDateTime createdAt, List<String> imageUrls,
                                List<String> fileUrls) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
        this.fileUrls = fileUrls;
    }

    public static DetailNoticeResponse of(Notice notice, List<String> imageUrls, List<String> fileUrls) {
        return new DetailNoticeResponse(notice.getTitle(), notice.getContent(), notice.getCreatedAt(), imageUrls, fileUrls);
    }

}
