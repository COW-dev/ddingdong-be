package ddingdong.ddingdongBE.domain.scorehistory.repository;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreHistoryRepository extends JpaRepository<ScoreHistory, Long> {

    List<ScoreHistory> findByClubId(Long clubId);
}
