package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UpdateNoticeRequest(
    @Schema(description = "공지사항 제목", example = "공지사항 제목입니다")
    String title,
    @Schema(description = "공지사항 내용", example = "카우 공지사항 내용입니다")
    String content,
    @ArraySchema(schema = @Schema(description = "이미지 Urls", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s"))
    List<String> imgUrls,
    @ArraySchema(schema = @Schema(description = "파일 Urls", example = "https://%s.s3.%s.amazonaws.com/%s/%s/%s"))
    List<String> fileUrls
) {

    public UpdateNoticeCommand toCommand(Long noticeId, List<MultipartFile> images, List<MultipartFile> files) {
        return UpdateNoticeCommand.builder()
            .title(title)
            .content(content)
            .imgUrls(imgUrls)
            .fileUrls(fileUrls)
            .noticeId(noticeId)
            .images(images)
            .files(files)
            .build();
    }

}
