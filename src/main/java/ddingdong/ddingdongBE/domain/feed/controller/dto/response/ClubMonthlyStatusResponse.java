package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record ClubMonthlyStatusResponse(
        @Schema(description = "연도", example = "2026")
        int year,
        @Schema(description = "월", example = "2")
        int month,
        @Schema(description = "이번 달 순위", example = "3")
        int rank,
        @Schema(description = "지난 달 순위", example = "5")
        int lastMonthRank,
        @Schema(description = "피드 가중치 점수", example = "150")
        long feedScore,
        @Schema(description = "조회 가중치 점수", example = "300")
        long viewScore,
        @Schema(description = "좋아요 가중치 점수", example = "50")
        long likeScore,
        @Schema(description = "댓글 가중치 점수", example = "100")
        long commentScore,
        @Schema(description = "총 가중치 점수", example = "600")
        long totalScore
) {

    public static ClubMonthlyStatusResponse from(ClubMonthlyStatusQuery query) {
        return ClubMonthlyStatusResponse.builder()
                .year(query.year())
                .month(query.month())
                .rank(query.rank())
                .lastMonthRank(query.lastMonthRank())
                .feedScore(query.feedScore())
                .viewScore(query.viewScore())
                .likeScore(query.likeScore())
                .commentScore(query.commentScore())
                .totalScore(query.totalScore())
                .build();
    }
}
