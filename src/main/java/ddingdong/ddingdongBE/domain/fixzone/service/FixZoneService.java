package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;

public interface FixZoneService {

    Long save(FixZone fixZone);

    FixZone getById(Long fixZoneId);

    List<FixZone> findAll();

    List<FixZone> findAllByClubId(Long clubId);

    void delete(Long fixZoneId);

    void deleteAllByClubId(Long clubId);

}
