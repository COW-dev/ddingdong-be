package ddingdong.ddingdongBE.domain.banner.controller.dto.request;

import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateBannerRequest",
        description = "어드민 - 배너 생성 요청"
)
public record CreateBannerRequest(
        @Schema(description = "연결 링크", example = "https://test-link.com")
        String link,
        @Schema(description = "웹 이미지 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
        @NotNull(message = "webImageKey는 필수입니다.")
        String webImageId,
        @Schema(description = "모바일 이미지 식별자", example = "0192c828-ffce-7ee8-94a8-d9d4c8cdec00")
        @NotNull(message = "mobileImageId 필수입니다.")
        String mobileImageId
) {

    public CreateBannerCommand toCommand(User user) {
        return new CreateBannerCommand(user, link, webImageId, mobileImageId);
    }

}
