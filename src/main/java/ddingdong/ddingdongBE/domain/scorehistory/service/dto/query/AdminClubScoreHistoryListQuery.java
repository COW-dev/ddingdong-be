package ddingdong.ddingdongBE.domain.scorehistory.service.dto.query;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.math.BigDecimal;
import java.util.List;

public record AdminClubScoreHistoryListQuery(
        BigDecimal clubTotalScore,
        List<ScoreHistory> scoreHistories
) {

    public static AdminClubScoreHistoryListQuery of(BigDecimal clubTotalScore, List<ScoreHistory> scoreHistories) {
        return new AdminClubScoreHistoryListQuery(clubTotalScore, scoreHistories);
    }
}
