package ddingdong.ddingdongBE.domain.banner.service.dto.command;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.user.entity.User;

public record CreateBannerCommand(
        User user,
        String link,
        String webImageId,
        String mobileImageId
) {

    public Banner toEntity() {
        return Banner.builder()
                .user(user)
                .link(link)
                .build();
    }

}
