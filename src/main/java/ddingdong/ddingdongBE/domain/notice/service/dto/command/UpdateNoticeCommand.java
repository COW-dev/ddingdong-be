package ddingdong.ddingdongBE.domain.notice.service.dto.command;

import ddingdong.ddingdongBE.domain.notice.entity.Notice;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateNoticeCommand(
    Long noticeId,
    String title,
    String content,
    List<ImageInfo> imageInfos,
    List<FileInfo> fileInfos
) {

    public Notice toEntity() {
        return Notice.builder()
            .title(title)
            .content(content)
            .build();
    }

    public record ImageInfo(
            String imageId,
            int order
    ) {

    }

    public record FileInfo(
            String fileId,
            int order
    ) {

    }

}
