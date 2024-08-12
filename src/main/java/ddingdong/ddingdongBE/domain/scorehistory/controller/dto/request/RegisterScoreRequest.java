package ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;

public record RegisterScoreRequest(
        String scoreCategory,
        String reason,
        float amount
) {

    public ScoreHistory toEntity(Club club) {
        return ScoreHistory.builder()
                .club(club)
                .amount(amount)
                .scoreCategory(ScoreCategory.from(scoreCategory))
                .reason(reason)
                .build();
    }
}
