package ddingdong.ddingdongBE.domain.feed.controller.dto.request;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateFeedRequest(
    @Schema(description = "활동 내용", example = "저희의 활동 내용은 이것입니다.")
    String activityContent,
    @Schema(description = "이미지/비디오 식별자 id", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
    String mediaId,
    @Schema(description = "컨텐츠 종류", example = "IMAGE")
    String contentType
) {

    public UpdateFeedCommand toCommand(Long feedId) {
        return UpdateFeedCommand.builder()
            .feedId(feedId)
            .activityContent(activityContent)
            .mediaId(mediaId)
            .contentType(contentType)
            .build();
    }
}
