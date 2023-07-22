package ddingdong.ddingdongBE.domain.banner.repository;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {

}
