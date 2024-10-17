package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.service.dto.query.AdminBannerListQuery;
import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;
import java.util.List;

public interface FacadeAdminBannerService {

    Long create(CreateBannerCommand command);

    List<AdminBannerListQuery> findAll();

    void delete(Long bannerId);

}
