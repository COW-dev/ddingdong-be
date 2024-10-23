package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.CreateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.command.UpdateFixZoneCommand;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralFixZoneQuery;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.CentralMyFixZoneListQuery;
import java.util.List;

public interface FacadeCentralFixZoneService {

    Long create(CreateFixZoneCommand command);

    List<CentralMyFixZoneListQuery> getMyFixZones(Long userId);

    CentralFixZoneQuery getFixZone(Long fixZoneId);

    Long update(UpdateFixZoneCommand command);

    void delete(Long fixZoneId);

}
