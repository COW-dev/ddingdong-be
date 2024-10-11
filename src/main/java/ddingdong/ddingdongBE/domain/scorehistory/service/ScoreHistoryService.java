package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.util.List;

public interface ScoreHistoryService {

    Long create(ScoreHistory scoreHistory);

    List<ScoreHistory> findAllByClubId(Long clubId);

}
