package ddingdong.ddingdongBE.domain.banner.repository;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.BannerFixture;
import ddingdong.ddingdongBE.common.fixture.UserFixture;
import ddingdong.ddingdongBE.common.support.DataJpaTestSupport;
import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.user.entity.User;
import ddingdong.ddingdongBE.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BannerRepositoryTest extends DataJpaTestSupport {

    @Autowired
    private BannerRepository bannerRepository;
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Banner 삭제 시 soft delete 되어 조회에서 제외된다.")
    @Test
    void softDeleteBanner() {
        // given
        User savedUser = userRepository.save(UserFixture.createGeneralUser());

        Banner banner = bannerRepository.save(BannerFixture.createBanner(savedUser));

        // when
        bannerRepository.delete(banner);
        entityManager.flush();
        entityManager.clear();

        // then
        Optional<Banner> found = bannerRepository.findById(banner.getId());
        assertThat(found).isEmpty();
    }

}
