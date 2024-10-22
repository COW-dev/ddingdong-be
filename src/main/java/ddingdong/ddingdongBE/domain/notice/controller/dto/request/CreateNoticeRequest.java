package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.CreateNoticeCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateNoticeRequest(
    @NotNull(message = "공지사항 제목은 필수 입력 사항입니다.")
    @Schema(description = "공지사항 제목", example = "공지사항 제목")
    String title,

    @NotNull(message = "공지사항 내용은 필수 입력 사항입니다.")
    @Schema(description = "공지사항 내용", example = "공지사항 내용")
    String content,

    @Schema(description = "공지사항 이미지 key 목록")
    List<String> imageKeys,

    @Schema(description = "공지사항 파일 key 목록")
    List<String> fileKeys
) {

    public CreateNoticeCommand toCommand(User user) {
        return CreateNoticeCommand.builder()
            .user(user)
            .title(title)
            .content(content)
            .imageKeys(imageKeys)
            .fileKeys(fileKeys)
            .build();
    }

}
