package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeUserBannerServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeUserBannerService facadeUserBannerService;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private UserRepository userRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();


    @DisplayName("유저: Banner 목록 조회")
    @Test
    void findAll() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        List<Banner> banners = fixtureMonkey.giveMeBuilder(Banner.class)
                .set("user", savedUser)
                .sampleList(5);
        bannerRepository.saveAll(banners);

        //when
        List<UserBannerListQuery> result = facadeUserBannerService.findAll();

        //then
        assertThat(result)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparing(UserBannerListQuery::id).reversed());
    }


}
