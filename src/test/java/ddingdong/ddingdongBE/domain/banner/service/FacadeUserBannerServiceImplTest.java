package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import ddingdong.ddingdongBE.common.fixture.BannerFixture;
import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FacadeUserBannerServiceImplTest extends TestContainerSupport {

    @Autowired
    private FacadeUserBannerService facadeUserBannerService;
    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;

    @MockitoBean
    S3FileService s3FileService;

    @BeforeEach
    void setUp() {
        fileMetaDataRepository.deleteAllInBatch();
        bannerRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저: 배너가 없으면 빈 리스트를 반환한다")
    @Test
    void findAllNoBannerReturnEmptyList() {
        // given & when
        List<UserBannerListQuery> result = facadeUserBannerService.findAll();

        // then
        assertThat(result).isEmpty();
    }


    @DisplayName("유저: Banner 목록 조회")
    @Test
    void findAll() {
        //given
        User savedUser = userRepository.save(UserFixture.createGeneralUser());
        List<Banner> banners = createBannersWithSize(savedUser, 5);
        bannerRepository.saveAll(banners);

        //when
        List<UserBannerListQuery> result = facadeUserBannerService.findAll();

        //then
        assertThat(result)
                .hasSize(5)
                .isSortedAccordingTo(Comparator.comparingLong(UserBannerListQuery::id).reversed());
    }

    @DisplayName("유저: 배너별로 web/mobile urlQuery가 정확히 매핑된다.")
    @Test
    void findAllMapsImagesByBannerId() {
        // given
        User savedUser = userRepository.save(UserFixture.createGeneralUser());

        Banner savedBanner1 = bannerRepository.save(BannerFixture.createBanner(savedUser));
        Banner savedBanner2 = bannerRepository.save(BannerFixture.createBanner(savedUser));

        // banner1 image
        FileMetaData b1Web = FileMetaDataFixture.fileMetaData(
                UUID.randomUUID(), savedBanner1.getId(), DomainType.BANNER_WEB_IMAGE, "b1-web-key",
                "b1-web.png"
        );
        FileMetaData b1Mobile = FileMetaDataFixture.fileMetaData(
                UUID.randomUUID(), savedBanner1.getId(), DomainType.BANNER_MOBILE_IMAGE, "b1-m-key",
                "b1-m.png"
        );

        // banner2 image
        FileMetaData b2Web = FileMetaDataFixture.fileMetaData(
                UUID.randomUUID(), savedBanner2.getId(), DomainType.BANNER_WEB_IMAGE, "b2-web-key",
                "b2-web.png"
        );
        FileMetaData b2Mobile = FileMetaDataFixture.fileMetaData(
                UUID.randomUUID(), savedBanner2.getId(), DomainType.BANNER_MOBILE_IMAGE, "b2-m-key",
                "b2-m.png"
        );

        fileMetaDataRepository.saveAll(List.of(b1Web, b1Mobile, b2Web, b2Mobile));

        UploadedFileUrlAndNameQuery b1WebQuery = mock(UploadedFileUrlAndNameQuery.class);
        UploadedFileUrlAndNameQuery b1MobileQuery = mock(UploadedFileUrlAndNameQuery.class);
        UploadedFileUrlAndNameQuery b2WebQuery = mock(UploadedFileUrlAndNameQuery.class);
        UploadedFileUrlAndNameQuery b2MobileQuery = mock(UploadedFileUrlAndNameQuery.class);

        given(s3FileService.getUploadedFileUrlAndName("b1-web-key", "b1-web.png")).willReturn(
                b1WebQuery);
        given(s3FileService.getUploadedFileUrlAndName("b1-m-key", "b1-m.png")).willReturn(
                b1MobileQuery);
        given(s3FileService.getUploadedFileUrlAndName("b2-web-key", "b2-web.png")).willReturn(
                b2WebQuery);
        given(s3FileService.getUploadedFileUrlAndName("b2-m-key", "b2-m.png")).willReturn(
                b2MobileQuery);

        // when
        List<UserBannerListQuery> result = facadeUserBannerService.findAll();

        // then
        UserBannerListQuery banner1Result = result.stream()
                .filter(it -> it.id().equals(savedBanner1.getId()))
                .findFirst()
                .orElseThrow();

        UserBannerListQuery banner2Result = result.stream()
                .filter(it -> it.id().equals(savedBanner2.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(banner1Result.webImageUrlQuery()).isSameAs(b1WebQuery);
        assertThat(banner1Result.mobileImageUrlQuery()).isSameAs(b1MobileQuery);

        assertThat(banner2Result.webImageUrlQuery()).isSameAs(b2WebQuery);
        assertThat(banner2Result.mobileImageUrlQuery()).isSameAs(b2MobileQuery);
    }

    private List<Banner> createBannersWithSize(User user, int size) {
        return Stream.generate(() -> BannerFixture.createBanner(user))
                .limit(size)
                .toList();
    }
}
