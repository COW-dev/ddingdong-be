package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneQuery;
import java.util.List;

public interface FacadeAdminFixZoneService {

    List<AdminFixZoneListQuery> getAll();

    AdminFixZoneQuery getFixZone(Long fixZoneId);

    void updateToComplete(Long fixZoneId);

    void delete(Long fixZoneId);

}
