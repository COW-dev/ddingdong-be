package ddingdong.ddingdongBE.domain.banner.service;

import ddingdong.ddingdongBE.domain.banner.service.dto.command.CreateBannerCommand;

public interface FacadeAdminBannerService {

    Long create(CreateBannerCommand command);

    void delete(Long bannerId);

}
