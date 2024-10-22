package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import java.util.List;

public interface FacadeAdminFixZoneService {

    List<AdminFixZoneListQuery> getAll();

    void updateToComplete(Long fixZoneId);

    void delete(Long fixZoneId);

}
