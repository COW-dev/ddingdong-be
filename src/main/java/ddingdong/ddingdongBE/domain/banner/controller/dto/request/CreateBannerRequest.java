package ddingdong.ddingdongBE.domain.banner.controller.dto.request;

import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "CreateBannerRequest",
        description = "어드민 - 배너 생성 요청"
)
public record CreateBannerRequest(
        @Schema(description = "웹 이미지 key", example = "local/file/2024-01-01/ddingdong/uuid")
        String webImageKey,
        @Schema(description = "모바일 이미지 key", example = "local/file/2024-01-01/ddingdong/uuid")
        String mobileImageKey
) {

    public CreateBannerCommand toCommand(User user) {
        return new CreateBannerCommand(user, webImageKey, mobileImageKey);
    }

}
