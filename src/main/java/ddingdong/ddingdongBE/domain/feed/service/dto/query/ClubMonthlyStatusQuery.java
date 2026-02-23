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

    private static final int FEED_WEIGHT = 10;
    private static final int VIEW_WEIGHT = 1;
    private static final int LIKE_WEIGHT = 3;
    private static final int COMMENT_WEIGHT = 5;

    public static ClubMonthlyStatusQuery from(int year, int month, ClubFeedRankingQuery ranking,
            int lastMonthRank) {
        long feedScore = ranking.feedCount() * FEED_WEIGHT;
        long viewScore = ranking.viewCount() * VIEW_WEIGHT;
        long likeScore = ranking.likeCount() * LIKE_WEIGHT;
        long commentScore = ranking.commentCount() * COMMENT_WEIGHT;

        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(ranking.rank())
                .lastMonthRank(lastMonthRank)
                .feedScore(feedScore)
                .viewScore(viewScore)
                .likeScore(likeScore)
                .commentScore(commentScore)
                .totalScore(feedScore + viewScore + likeScore + commentScore)
                .build();
    }

    public static ClubMonthlyStatusQuery createEmpty(int year, int month) {
        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(0)
                .lastMonthRank(0)
                .feedScore(0)
                .viewScore(0)
                .likeScore(0)
                .commentScore(0)
                .totalScore(0)
                .build();
    }
}
