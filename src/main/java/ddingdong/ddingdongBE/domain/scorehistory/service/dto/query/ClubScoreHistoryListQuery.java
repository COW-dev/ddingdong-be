package ddingdong.ddingdongBE.domain.scorehistory.service.dto.query;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.math.BigDecimal;
import java.util.List;

public record ClubScoreHistoryListQuery(
        BigDecimal clubTotalScore,
        List<ScoreHistory> scoreHistories
) {

    public static ClubScoreHistoryListQuery of(BigDecimal clubTotalScore, List<ScoreHistory> scoreHistories) {
        return new ClubScoreHistoryListQuery(clubTotalScore, scoreHistories);
    }
}
