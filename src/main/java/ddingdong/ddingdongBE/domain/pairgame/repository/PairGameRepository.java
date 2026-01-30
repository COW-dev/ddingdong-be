package ddingdong.ddingdongBE.domain.pairgame.repository;

import ddingdong.ddingdongBE.domain.pairgame.entity.PairGameApplier;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PairGameRepository extends JpaRepository<PairGameApplier, Long> {
}
