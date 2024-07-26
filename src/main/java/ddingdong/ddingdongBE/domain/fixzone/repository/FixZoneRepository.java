package ddingdong.ddingdongBE.domain.fixzone.repository;

import ddingdong.ddingdongBE.domain.fixzone.entity.FixZone;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FixZoneRepository extends JpaRepository<FixZone, Long> {

    List<FixZone> findAllByClubId(Long clubId);

}
