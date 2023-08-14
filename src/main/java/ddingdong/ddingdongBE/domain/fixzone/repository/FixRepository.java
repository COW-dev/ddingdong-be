package ddingdong.ddingdongBE.domain.fixzone.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ddingdong.ddingdongBE.domain.fixzone.entitiy.Fix;

@Repository
public interface FixRepository extends JpaRepository<Fix, Long> {
}
