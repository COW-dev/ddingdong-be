package ddingdong.ddingdongBE.domain.fixzone.service;

import ddingdong.ddingdongBE.common.exception.PersistenceException.ResourceNotFound;
import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import ddingdong.ddingdongBE.domain.fixzone.repository.FixZoneRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GeneralFixZoneService implements FixZoneService {

    private final FixZoneRepository fixZoneRepository;

    @Override
    @Transactional
    public Long save(FixZone fixZone) {
        FixZone savedFixZone = fixZoneRepository.save(fixZone);
        return savedFixZone.getId();
    }

    @Override
    public FixZone getById(Long fixZoneId) {
        return fixZoneRepository.findById(fixZoneId)
                .orElseThrow(() -> new ResourceNotFound("FixZone(id=" + fixZoneId + ")를 찾을 수 없습니다."));
    }

    @Override
    public List<FixZone> findAll() {
        return fixZoneRepository.findAll();
    }

    @Override
    public List<FixZone> findAllByClubId(Long clubId) {
        return fixZoneRepository.findAllByClubId(clubId);
    }

    @Override
    @Transactional
    public void delete(Long fixZoneId) {
        FixZone fixZone = getById(fixZoneId);
        this.fixZoneRepository.delete(fixZone);
    }
}
