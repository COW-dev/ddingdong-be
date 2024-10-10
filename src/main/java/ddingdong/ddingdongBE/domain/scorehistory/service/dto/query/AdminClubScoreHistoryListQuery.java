package ddingdong.ddingdongBE.domain.scorehistory.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.util.List;

public record AdminClubScoreHistoryListQuery(
        Club club,
        List<ScoreHistory> scoreHistories
) {

    public static AdminClubScoreHistoryListQuery of(Club club, List<ScoreHistory> scoreHistories) {
        return new AdminClubScoreHistoryListQuery(club, scoreHistories);
    }
}
