package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateNoticeRequest(
    @NotNull(message = "공지사항 제목은 필수 입력 사항입니다.")
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,

    @NotNull(message = "공지사항 내용은 필수 입력 사항입니다.")
    @Schema(description = "공지사항 내용", example = "카우 공지사항 내용입니다")
    String content,

    @Schema(description = "공지사항 이미지 key 목록")
    List<String> imageKeys,

    @Schema(description = "공지사항 파일 key 목록")
    List<String> fileKeys
) {

    public UpdateNoticeCommand toCommand() {
        return UpdateNoticeCommand.builder()
            .title(title)
            .content(content)
            .imageKeys(imageKeys)
            .fileKeys(fileKeys)
            .build();
    }

}
