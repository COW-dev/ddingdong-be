package ddingdong.ddingdongBE.domain.banner.service.dto.command;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.user.entity.User;

public record CreateBannerCommand(
        User user,
        String webImageKey,
        String mobileImageKey
) {

    public Banner toEntity() {
        return Banner.builder()
                .user(user)
                .webImageKey(webImageKey)
                .mobileImageKey(mobileImageKey)
                .build();
    }

}
