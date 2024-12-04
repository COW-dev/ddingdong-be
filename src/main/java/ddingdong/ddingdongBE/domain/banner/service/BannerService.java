package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.entity.Banner;
import java.util.List;

public interface BannerService {

    Long save(Banner banner);

    List<Banner> findAll();

    void delete(Long bannerId);

}
