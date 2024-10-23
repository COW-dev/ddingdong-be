package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.service.dto.query.AdminFixZoneListQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FacadeAdminFixZoneServiceImpl implements FacadeAdminFixZoneService {

    private final FixZoneService fixZoneService;

    @Override
    public List<AdminFixZoneListQuery> getAll() {
        return fixZoneService.findAll().stream()
            .map(AdminFixZoneListQuery::from)
            .toList();
    }

    @Override
    @Transactional
    public void updateToComplete(Long fixZoneId) {
        FixZone fixZone = fixZoneService.getById(fixZoneId);
        fixZone.updateToComplete();
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        fixZoneService.delete(fixZoneId);
    }
}
