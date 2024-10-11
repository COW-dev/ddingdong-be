package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.ClubScoreHistoryListQuery;

public interface FacadeClubScoreHistoryService {

    ClubScoreHistoryListQuery findMyScoreHistories(Long userId);

}
