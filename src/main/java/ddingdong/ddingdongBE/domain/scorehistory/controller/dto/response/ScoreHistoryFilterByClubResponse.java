package ddingdong.ddingdongBE.domain.scorehistory.controller.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import ddingdong.ddingdongBE.domain.club.entity.Club;
import ddingdong.ddingdongBE.domain.scorehistory.entity.ScoreHistory;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Schema(
        name = "ScoreHistoryFilterByClubResponse",
        description = "어드민 - 동아리 점수 변동 내역 목록 응답"
)
@Builder
public record ScoreHistoryFilterByClubResponse(

        @Schema(description = "동아리 총 점수", example = "50")
        BigDecimal totalScore,
        @ArraySchema(schema = @Schema(description = "점수내역 목록", implementation = ScoreHistoryResponse.class))
        List<ScoreHistoryResponse> scoreHistories
) {

    public static ScoreHistoryFilterByClubResponse of(Club club, List<ScoreHistoryResponse> scoreHistories) {
        return ScoreHistoryFilterByClubResponse.builder()
                .totalScore(club.getScore().getValue())
                .scoreHistories(scoreHistories)
                .build();
    }

    @Schema(
            name = "ScoreHistoryResponse",
            description = "점수 변동 내역 응답"
    )
    @Builder
    public record ScoreHistoryResponse(

            @Schema(description = "점수 내역 카테고리", example = "활동보고서")
            String scoreCategory,
            @Schema(description = "점수 내역 이유", example = "활동보고서 작성")
            String reason,
            @Schema(description = "변동 점수", example = "10")
            BigDecimal amount,
            @Schema(description = "작성일", example = "2024-01-01")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime createdAt
    ) {

        public static ScoreHistoryResponse from(ScoreHistory scoreHistory) {
            return ScoreHistoryResponse.builder()
                    .scoreCategory(scoreHistory.getScoreCategory().getCategory())
                    .reason(scoreHistory.getReason())
                    .amount(scoreHistory.getAmount())
                    .createdAt(scoreHistory.getCreatedAt())
                    .build();
        }

    }
}
