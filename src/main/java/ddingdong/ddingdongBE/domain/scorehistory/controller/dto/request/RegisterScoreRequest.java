package ddingdong.ddingdongBE.domain.scorehistory.controller.dto.request;

import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreCategory;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import lombok.Getter;

@Getter
public class RegisterScoreRequest {

    private String scoreCategory;

    private String reason;

    private float amount;

    public ScoreHistory toEntity(Club club) {
        return ScoreHistory.builder()
                .club(club)
                .amount(amount)
                .scoreCategory(ScoreCategory.of(scoreCategory))
                .reason(reason)
                .build();
    }
}
