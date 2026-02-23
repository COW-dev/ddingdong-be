package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import lombok.Builder;

@Builder
public record ClubMonthlyStatusResponse(
        int year,
        int month,
        int rank,
        int lastMonthRank,
        long feedScore,
        long viewScore,
        long likeScore,
        long commentScore,
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
