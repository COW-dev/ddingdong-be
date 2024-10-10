package ddingdong.ddingdongBE.domain.scorehistory.service.dto.query;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.util.List;
import lombok.Builder;

@Builder
public record ClubScoreHistoryListQuery(
        Club club,
        List<ScoreHistory> scoreHistories
) {

    public static ClubScoreHistoryListQuery of(
            Club club,
            List<ScoreHistory> scoreHistories) {
        return ClubScoreHistoryListQuery.builder()
                .club(club)
                .scoreHistories(scoreHistories)
                .build();
    }
}
