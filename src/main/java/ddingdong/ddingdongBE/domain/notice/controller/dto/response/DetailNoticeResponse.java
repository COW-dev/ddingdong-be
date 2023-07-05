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

    private DetailNoticeResponse(String title, String content, LocalDateTime createdAt, List<String> imageUrls) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrls = imageUrls;
    }

    public static DetailNoticeResponse of(Notice notice, List<String> imageUrls) {
        return new DetailNoticeResponse(notice.getTitle(), notice.getContent(), notice.getCreatedAt(), imageUrls);
    }

}
