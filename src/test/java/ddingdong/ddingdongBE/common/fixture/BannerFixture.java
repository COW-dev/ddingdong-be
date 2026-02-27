package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.entity.BannerType;
import ddingdong.ddingdongBE.domain.user.entity.User;

public class BannerFixture {

    public static Banner createBanner(User user) {
        return Banner.builder()
                .id(null)
                .user(user)
                .link("testLink")
                .build();
    }

    public static Banner createRankingBanner(String link) {
        return Banner.builder()
                .id(null)
                .bannerType(BannerType.FEED_RANKING)
                .link(link)
                .build();
    }
}
