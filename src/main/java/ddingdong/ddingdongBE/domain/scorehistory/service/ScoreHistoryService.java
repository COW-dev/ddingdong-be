package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.util.List;

public interface ScoreHistoryService {

    Long save(ScoreHistory scoreHistory);

    List<ScoreHistory> findAllByClubId(Long clubId);

}
