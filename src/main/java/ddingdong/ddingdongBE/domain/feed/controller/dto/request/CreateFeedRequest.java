package ddingdong.ddingdongBE.domain.feed.controller.dto.request;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.CreateFeedCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateFeedRequest(
    @Schema(description = "활동 내용", example = "저희의 활동 내용은 이것입니다.")
    String activityContent,
    @Schema(description = "이미지/비디오 식별자 id", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
    String mediaId,
    @Schema(description = "컨텐츠 종류", example = "IMAGE")
    String contentType
) {

    public CreateFeedCommand toCommand(User user) {
        return CreateFeedCommand.builder()
            .activityContent(activityContent)
            .mediaId(mediaId)
            .contentType(contentType)
            .user(user)
            .build();
    }
}
