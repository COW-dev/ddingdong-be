package ddingdong.ddingdongBE.domain.notice.controller.dto.request;

import ddingdong.ddingdongBE.domain.notice.service.dto.command.UpdateNoticeCommand;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public record UpdateNoticeRequest(
    String title,
    String content,
    List<String> imgUrls,
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
