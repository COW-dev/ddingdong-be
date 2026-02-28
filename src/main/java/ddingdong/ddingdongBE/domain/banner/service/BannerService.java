package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import ddingdong.ddingdongBE.domain.banner.entity.BannerType;
import java.util.List;

public interface BannerService {

    Long save(Banner banner);

    List<Banner> findAll();

    List<Banner> getAllByBannerType(BannerType bannerType);

    void delete(Long bannerId);

}
