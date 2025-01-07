package ddingdong.ddingdongBE.domain.feed.controller.dto.request;

import ddingdong.ddingdongBE.domain.feed.service.dto.command.UpdateFeedCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UpdateFeedRequest(
    @Schema(description = "활동 내용", example = "저희의 활동 내용은 이것입니다.")
    @NotNull(message = "활동내용은 null 값이 될 수 없습니다.")
    String activityContent
) {

    public UpdateFeedCommand toCommand(Long feedId) {
        return UpdateFeedCommand.builder()
            .feedId(feedId)
            .activityContent(activityContent)
            .build();
    }
}
