package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.service.dto.query.UserBannerListQuery;
import java.util.List;

public interface FacadeUserBannerService {

    List<UserBannerListQuery> findAll();

}
