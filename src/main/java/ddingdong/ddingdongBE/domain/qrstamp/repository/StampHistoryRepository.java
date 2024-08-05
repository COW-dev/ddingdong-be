package ddingdong.ddingdongBE.domain.qrstamp.repository;

import ddingdong.ddingdongBE.domain.qrstamp.entity.StampHistory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StampHistoryRepository extends JpaRepository<StampHistory, Long> {

    Optional<StampHistory> findStampHistoryByStudentNumber(String studentNumber);

    Optional<StampHistory> findStampHistoryByStudentNameAndStudentNumber(String studentName, String studentNumber);

    List<StampHistory> findAllByCertificationImageUrlIsNotNull();

}
