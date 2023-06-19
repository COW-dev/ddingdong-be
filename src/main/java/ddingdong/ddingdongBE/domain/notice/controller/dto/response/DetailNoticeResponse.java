package ddingdong.ddingdongBE.domain.notice.controller.dto.response;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.time.LocalDateTime;
import java.util.List;

public class DetailNoticeResponse {

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private List<String> imageUrls;

    private DetailNoticeResponse(String title, String content, LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    public static DetailNoticeResponse from(Notice notice) {
        return new DetailNoticeResponse(notice.getTitle(), notice.getContent(), notice.getCreatedAt());
    }

}
