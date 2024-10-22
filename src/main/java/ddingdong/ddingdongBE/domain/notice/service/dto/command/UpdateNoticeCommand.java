package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateNoticeCommand(
    String title,
    String content,
    List<String> imageKeys,
    List<String> fileKeys
) {

    public Notice toEntity() {
        return Notice.builder()
            .title(title)
            .content(content)
            .imageKeys(imageKeys)
            .fileKeys(fileKeys)
            .build();
    }
}
