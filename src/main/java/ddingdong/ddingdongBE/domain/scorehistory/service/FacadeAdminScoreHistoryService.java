package ddingdong.ddingdongBE.domain.scorehistory.service;

import ddingdong.ddingdongBE.domain.scorehistory.service.dto.command.CreateScoreHistoryCommand;
import ddingdong.ddingdongBE.domain.scorehistory.service.dto.query.AdminClubScoreHistoryListQuery;

public interface FacadeAdminScoreHistoryService {

    Long create(CreateScoreHistoryCommand command);

    AdminClubScoreHistoryListQuery findAllByClubId(Long clubId);

}
