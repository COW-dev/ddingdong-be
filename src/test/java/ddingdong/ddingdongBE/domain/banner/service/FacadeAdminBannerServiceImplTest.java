package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FacadeAdminBannerServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeAdminBannerService facadeAdminBannerService;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("어드민: Banner 생성")
    @Test
    void create() {
        //given
        UUID webUUID = UuidCreator.getTimeOrderedEpoch();
        UUID mobileUUID = UuidCreator.getTimeOrderedEpoch();
        String webImageKey = "test/file/2024-01-01/" + webUUID.toString();
        String mobileImageKey = "test/file/2024-01-01/" + mobileUUID.toString();
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        CreateBannerCommand command = new CreateBannerCommand(savedUser, webImageKey, mobileImageKey);

        //when
        Long createdBannerId = facadeAdminBannerService.create(command);

        //then
        Banner createdBanner = bannerRepository.findById(createdBannerId).orElseThrow();
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findAll();
        assertThat(createdBanner)
                .extracting("id", "user.id", "webImageKey", "mobileImageKey")
                .contains(
                        createdBanner.getId(),
                        savedUser.getId(),
                        webImageKey,
                        mobileImageKey
                );
        assertThat(fileMetaDataList).hasSize(2);
    }

    @DisplayName("어드민: Banner 목록 조회")
    @Test
    void findAll() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        List<Banner> banners = fixtureMonkey.giveMeBuilder(Banner.class)
                .set("user", savedUser)
                .set("deletedAt", null)
                .set("webImageKey", "test/file/2024-01-01/test/uuid" )
                .set("mobileImageKey", "test/file/2024-01-01/test/uuid" )
                .sampleList(5);
        bannerRepository.saveAll(banners);

        //when
        List<AdminBannerListQuery> result = facadeAdminBannerService.findAll();

        //then
        assertThat(result)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparing(AdminBannerListQuery::id).reversed());
    }

    @DisplayName("어드민: Banner 삭제")
    @Test
    void delete() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Banner banner = fixtureMonkey.giveMeBuilder(Banner.class)
                .set("user", savedUser)
                .set("deletedAt", null)
                .sample();
        Banner savedBanner = bannerRepository.save(banner);

        //when
        facadeAdminBannerService.delete(savedBanner.getId());

        //then
        List<Banner> result = bannerRepository.findAll();
        assertThat(result).isEmpty();
    }
}
