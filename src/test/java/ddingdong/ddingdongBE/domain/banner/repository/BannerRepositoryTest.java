package ddingdong.ddingdongBE.domain.banner.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.f4b6a3.uuid.UuidCreator;
import com.navercorp.fixturemonkey.FixtureMonkey;
import ddingdong.ddingdongBE.common.support.FixtureMonkeyFactory;
import ddingdong.ddingdongBE.common.support.TestContainerSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.repository.dto.BannerWithFileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileMetaData;
import ddingdong.ddingdongBE.domain.filemetadata.entity.FileStatus;
import ddingdong.ddingdongBE.domain.filemetadata.repository.FileMetaDataRepository;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BannerRepositoryTest extends TestContainerSupport {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private FileMetaDataRepository fileMetaDataRepository;
    @Autowired
    private UserRepository userRepository;

    private final FixtureMonkey fixtureMonkey = FixtureMonkeyFactory.getNotNullBuilderIntrospectorMonkey();

    @DisplayName("배너 조회 - with FileMetaData")
    @Test
    void findAllBannersWithFileMetaData() {
        //given
        User savedUser = userRepository.save(fixtureMonkey.giveMeOne(User.class));
        Banner savedBanner = bannerRepository.save(fixtureMonkey.giveMeBuilder(Banner.class)
                .set("user", savedUser)
                .set("deletedAt", null)
                .sample());
        FileMetaData savedFileMetaData = fileMetaDataRepository.save(FileMetaData.builder()
                .id(UuidCreator.getTimeOrderedEpoch())
                .fileKey("test")
                .fileName("test")
                .entityId(savedBanner.getId())
                .fileStatus(FileStatus.COUPLED)
                .build());

        //when
        List<BannerWithFileMetaData> result = bannerRepository.findAllBannersWithFileMetaData();

        //then
        assertThat(result).hasSize(1)
                .extracting("banner.id", "fileMetaData.id", "fileMetaData.entityId")
                .containsOnly(Tuple.tuple(savedBanner.getId(), savedFileMetaData.getId(), savedBanner.getId()));

    }

}
