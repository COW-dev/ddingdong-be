package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubMonthlyStatusQuery(
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

    public static ClubMonthlyStatusQuery of(int year, int month, int rank, int lastMonthRank,
            long feedScore, long viewScore, long likeScore, long commentScore) {
        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(rank)
                .lastMonthRank(lastMonthRank)
                .feedScore(feedScore)
                .viewScore(viewScore)
                .likeScore(likeScore)
                .commentScore(commentScore)
                .totalScore(feedScore + viewScore + likeScore + commentScore)
                .build();
    }

    public static ClubMonthlyStatusQuery createEmpty(int year, int month, int lastMonthRank) {
        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(0)
                .lastMonthRank(lastMonthRank)
                .feedScore(0)
                .viewScore(0)
                .likeScore(0)
                .commentScore(0)
                .totalScore(0)
                .build();
    }
}
