package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record CreateNoticeRequest(
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,
    @Schema(description = "공지사항 내용", example = "카우 공지사항 내용입니다")
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
