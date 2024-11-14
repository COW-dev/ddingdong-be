package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateNoticeCommand(
    User user,
    String title,
    String content,
    List<String> imageIds,
    List<String> fileIds
) {

    public Notice toEntity() {
        return Notice.builder()
            .user(user)
            .title(title)
            .content(content)
            .build();
    }

}
