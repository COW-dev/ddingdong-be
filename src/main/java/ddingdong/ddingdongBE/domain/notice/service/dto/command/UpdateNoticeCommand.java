package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.List;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record UpdateNoticeCommand(
    String title,
    String content,
    List<String> imgUrls,
    List<String> fileUrls,
    Long noticeId,
    List<MultipartFile> images,
    List<MultipartFile> files
) {

    public Notice toEntity() {
        return Notice.builder()
            .title(title)
            .content(content)
            .build();
    }
}
