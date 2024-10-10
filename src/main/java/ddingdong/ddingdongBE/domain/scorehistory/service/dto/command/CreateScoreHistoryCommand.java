package ddingdong.ddingdongBE.domain.scorehistory.service.dto.command;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CreateScoreHistoryCommand(
        Long clubId,
        ScoreCategory scoreCategory,
        String reason,
        BigDecimal amount
) {

    public ScoreHistory toEntity(Club club) {
        return ScoreHistory.builder()
                .club(club)
                .amount(amount)
                .scoreCategory(scoreCategory)
                .reason(reason)
                .build();
    }
}
