package ddingdong.ddingdongBE.domain.banner.controller.dto.request;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.user.entity.User;
import lombok.Getter;

@Getter
public class CreateBannerRequest {

    private String title;

    private String subTitle;

    private String colorCode;

    public Banner toEntity(User user) {
        return Banner.builder()
                .user(user)
                .title(title)
                .subTitle(subTitle)
                .colorCode(colorCode)
                .build();
    }
}
