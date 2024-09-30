package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CreateNoticeRequest(
    String title,
    String content
) {

    public CreateNoticeCommand toCommand(User user, List<MultipartFile> images, List<MultipartFile> files) {
        return CreateNoticeCommand.builder()
            .user(user)
            .title(title)
            .content(content)
            .images(images)
            .files(files)
            .build();
    }
}
