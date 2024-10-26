package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import ddingdong.ddingdongBE.common.vo.FileInfo;
import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateNoticeCommand(
    User user,
    String title,
    String content,
    List<String> imageKeys,
    List<FileInfo> fileInfos
) {

    public Notice toEntity(String fileInfos) {
        return Notice.builder()
            .user(user)
            .title(title)
            .content(content)
            .imageKeys(imageKeys)
            .fileInfos(fileInfos)
            .build();
    }

}
