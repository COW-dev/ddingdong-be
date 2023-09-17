package ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response;

import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ScoreHistoryFilterByClubResponse {
    private String scoreCategory;

    private String reason;

    private float amount;

    private float remainingScore;

    private LocalDateTime createdAt;

    @Builder
    public ScoreHistoryFilterByClubResponse(String scoreCategory, String reason, float amount, float remainingScore, LocalDateTime createdAt) {
        this.scoreCategory = scoreCategory;
        this.reason = reason;
        this.amount = amount;
        this.remainingScore = remainingScore;
        this.createdAt = createdAt;
    }

    public static ScoreHistoryFilterByClubResponse of(final ScoreHistory scoreHistory) {
        return ScoreHistoryFilterByClubResponse.builder()
                .scoreCategory(scoreHistory.getScoreCategory().getCategory())
                .reason(scoreHistory.getReason())
                .amount(scoreHistory.getAmount())
                .remainingScore(scoreHistory.getRemainingScore())
                .createdAt(scoreHistory.getCreatedAt())
                .build();
    }
}
