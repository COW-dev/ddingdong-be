package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.user.entity.User;

public class BannerFixture {

    public static Banner createBanner(User user) {
        return Banner.builder()
                .id(null)
                .user(user)
                .link("testLink")
                .build();
    }
}
