package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.BannerRepository;
import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.filemetadata.entity.DomainType;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private EntityManager entityManager;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();


    @BeforeEach
    void resetAutoIncrement() {
        entityManager.createNativeQuery("ALTER TABLE banner AUTO_INCREMENT = 1").executeUpdate();
        fileMetaDataRepository.deleteAll();
        bannerRepository.deleteAll();
    }

    @DisplayName("어드민: Banner 생성")
    @Test
    void create() {
        //given
        String link = "testLink";
        UUID webImageId = UuidCreator.getTimeOrderedEpoch();
        UUID mobileImageId = UuidCreator.getTimeOrderedEpoch();
        User savedUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class).set("id", null).sample());
        CreateBannerCommand command = new CreateBannerCommand(
                savedUser,
                link,
                webImageId.toString(),
                mobileImageId.toString()
        );

        fileMetaDataRepository.saveAll(List.of(
                        FileMetaData.builder()
                                .id(webImageId)
                                .fileKey("test")
                                .fileName("test")
                                .fileStatus(FileStatus.PENDING)
                                .build(),
                        FileMetaData.builder()
                                .id(mobileImageId)
                                .fileKey("test")
                                .fileName("test")
                                .fileStatus(FileStatus.PENDING)
                                .build()
                )
        );

        //when
        Long createdBannerId = facadeAdminBannerService.create(command);

        //then
        Banner createdBanner = bannerRepository.findById(createdBannerId).orElseThrow();
        List<FileMetaData> fileMetaDataList = fileMetaDataRepository.findAllByEntityIdWithFileStatus(
                createdBannerId, FileStatus.COUPLED);
        assertThat(createdBanner)
                .extracting("id", "user.id", "link")
                .contains(
                        createdBanner.getId(),
                        savedUser.getId(),
                        link
                );
        assertThat(fileMetaDataList).hasSize(2)
                .extracting(FileMetaData::getId, FileMetaData::getFileStatus)
                .containsExactlyInAnyOrder(
                        tuple(webImageId, FileStatus.COUPLED),
                        tuple(mobileImageId, FileStatus.COUPLED)
                );
    }

    @DisplayName("어드민: Banner 목록 조회")
    @Test
    void findAll() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class).set("id", null).sample());
        bannerRepository.saveAll(List.of(
                fixtureMonkey.giveMeBuilder(Banner.class)
                        .setNull("id")
                        .set("user", savedUser)
                        .set("deletedAt", null)
                        .sample(),
                fixtureMonkey.giveMeBuilder(Banner.class)
                        .setNull("id")
                        .set("user", savedUser)
                        .set("deletedAt", null)
                        .sample()
        ));

        FileMetaData fileMetaData1 = fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", UUID.randomUUID())
                .set("entityId", 1L)
                .set("fileKey", "/test/FILE/2024-01-01/cow/test")
                .set("domainType", DomainType.BANNER_WEB_IMAGE)
                .set("fileStatus", FileStatus.COUPLED)
                .sample();
        FileMetaData fileMetaData2 = fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", UUID.randomUUID())
                .set("entityId", 1L)
                .set("fileKey", "/test/FILE/2024-01-01/cow/test")
                .set("domainType", DomainType.BANNER_MOBILE_IMAGE)
                .set("fileStatus", FileStatus.COUPLED)
                .sample();
        FileMetaData fileMetaData3 = fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", UUID.randomUUID())
                .set("entityId", 2L)
                .set("fileKey", "/test/FILE/2024-01-01/cow/test")
                .set("domainType", DomainType.BANNER_WEB_IMAGE)
                .set("fileStatus", FileStatus.COUPLED)
                .sample();
        FileMetaData fileMetaData4 = fixtureMonkey.giveMeBuilder(FileMetaData.class)
                .set("id", UUID.randomUUID())
                .set("entityId", 2L)
                .set("fileKey", "/test/FILE/2024-01-01/cow/test")
                .set("domainType", DomainType.BANNER_MOBILE_IMAGE)
                .set("fileStatus", FileStatus.COUPLED)
                .sample();
        fileMetaDataRepository.saveAll(List.of(fileMetaData1, fileMetaData2, fileMetaData3, fileMetaData4));

        //when
        List<AdminBannerListQuery> result = facadeAdminBannerService.findAll();

        //then
        assertThat(result)
                .hasSize(2)
                .isSortedAccordingTo(Comparator.comparing(AdminBannerListQuery::id).reversed())
                .satisfies(queries -> {
                    AdminBannerListQuery firstBanner = queries.get(0);
                    AdminBannerListQuery secondBanner = queries.get(1);

                    // id=2인 배너 검증 (역순이므로 첫 번째)
                    assertThat(firstBanner)
                            .satisfies(banner -> {
                                assertThat(banner.id()).isEqualTo(2L);
                                assertThat(banner.webImageUrlQuery()).isNotNull();
                                assertThat(banner.mobileImageUrlQuery()).isNotNull();
                            });

                    // id=1인 배너 검증
                    assertThat(secondBanner)
                            .satisfies(banner -> {
                                assertThat(banner.id()).isEqualTo(1L);
                                assertThat(banner.webImageUrlQuery()).isNotNull();
                                assertThat(banner.mobileImageUrlQuery()).isNotNull();
                            });
                });
    }

    @DisplayName("어드민: Banner 삭제")
    @Test
    void delete() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeBuilder(User.class).set("id", null).sample());
        Banner banner = fixtureMonkey.giveMeBuilder(Banner.class)
                .setNull("id")
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
