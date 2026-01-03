package ddingdong.ddingdongBE.domain.banner.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.f4b6a3.uuid.UuidCreator;
import ddingdong.ddingdongBE.common.fixture.BannerFixture;
import ddingdong.ddingdongBE.common.fixture.FileMetaDataFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
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
import ddingdong.ddingdongBE.file.service.S3FileService;
import ddingdong.ddingdongBE.file.service.dto.query.UploadedFileUrlAndNameQuery;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

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

    @MockitoBean
    private S3FileService s3FileService;

    @BeforeEach
    void setUp() {
        fileMetaDataRepository.deleteAllInBatch();
        bannerRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("어드민: Banner 생성")
    @Test
    void create() {
        //given
        String link = "testLink";
        UUID webImageId = UuidCreator.getTimeOrderedEpoch();
        UUID mobileImageId = UuidCreator.getTimeOrderedEpoch();
        User savedUser = userRepository.save(UserFixture.createGeneralUser());
        CreateBannerCommand command = new CreateBannerCommand(
                savedUser,
                link,
                webImageId.toString(),
                mobileImageId.toString()
        );

        fileMetaDataRepository.saveAll(List.of(
                FileMetaDataFixture.createFileMetaDataWithFileStatus(webImageId, FileStatus.PENDING),
                FileMetaDataFixture.createFileMetaDataWithFileStatus(mobileImageId, FileStatus.PENDING)
        ));

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

    @DisplayName("어드민: Banner 목록 조회 - 배너별로 web/mobile urlQuery가 정확히 매핑된다.")
    @Test
    void findAll() {
        // given
        User savedUser = userRepository.save(UserFixture.createGeneralUser());

        Banner savedBanner1 = bannerRepository.save(BannerFixture.createBanner(savedUser));
        Banner savedBanner2 = bannerRepository.save(BannerFixture.createBanner(savedUser));

        FileMetaData b1Web = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner1.getId(), DomainType.BANNER_WEB_IMAGE, "b1-web-key",
                "b1-web.png"
        );
        FileMetaData b1Mobile = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner1.getId(), DomainType.BANNER_MOBILE_IMAGE, "b1-m-key",
                "b1-m.png"
        );
        FileMetaData b2Web = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner2.getId(), DomainType.BANNER_WEB_IMAGE, "b2-web-key",
                "b2-web.png"
        );
        FileMetaData b2Mobile = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner2.getId(), DomainType.BANNER_MOBILE_IMAGE, "b2-m-key",
                "b2-m.png"
        );
        fileMetaDataRepository.saveAll(
                List.of(b1Web, b1Mobile, b2Web, b2Mobile));

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
        List<AdminBannerListQuery> result = facadeAdminBannerService.findAll();

        // then
        assertThat(result)
                .hasSize(2)
                .isSortedAccordingTo(Comparator.comparing(AdminBannerListQuery::id).reversed());

        AdminBannerListQuery banner1Result = result.stream()
                .filter(it -> it.id().equals(savedBanner1.getId()))
                .findFirst()
                .orElseThrow();

        AdminBannerListQuery banner2Result = result.stream()
                .filter(it -> it.id().equals(savedBanner2.getId()))
                .findFirst()
                .orElseThrow();

        assertThat(banner1Result.webImageUrlQuery()).isEqualTo(b1WebQuery);
        assertThat(banner1Result.mobileImageUrlQuery()).isEqualTo(b1MobileQuery);
        assertThat(banner2Result.webImageUrlQuery()).isEqualTo(b2WebQuery);
        assertThat(banner2Result.mobileImageUrlQuery()).isEqualTo(b2MobileQuery);
    }

    @DisplayName("어드민: Banner 삭제")
    @Test
    void delete() {
        //given
        User savedUser = userRepository.save(UserFixture.createGeneralUser());
        Banner savedBanner = bannerRepository.save(BannerFixture.createBanner(savedUser));
        FileMetaData web = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner.getId(), DomainType.BANNER_WEB_IMAGE, "web-key",
                "web.png"
        );
        FileMetaData mobile = FileMetaDataFixture.createCoupledFileMetaData(
                UUID.randomUUID(), savedBanner.getId(), DomainType.BANNER_MOBILE_IMAGE, "m-key",
                "m.png"
        );
        fileMetaDataRepository.saveAll(List.of(web, mobile));

        //when
        facadeAdminBannerService.delete(savedBanner.getId());

        //then
        List<Banner> result = bannerRepository.findAll();
        assertThat(result).isEmpty();

        List<FileMetaData> deleted = fileMetaDataRepository.findAllByEntityIdWithFileStatus(
                savedBanner.getId(), FileStatus.DELETED);

        assertThat(deleted).hasSize(2)
                .extracting(FileMetaData::getDomainType, FileMetaData::getFileStatus)
                .containsExactlyInAnyOrder(
                        tuple(DomainType.BANNER_WEB_IMAGE, FileStatus.DELETED),
                        tuple(DomainType.BANNER_MOBILE_IMAGE, FileStatus.DELETED)
                );
    }
}
