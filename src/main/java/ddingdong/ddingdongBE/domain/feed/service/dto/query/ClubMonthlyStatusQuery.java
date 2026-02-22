package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubMonthlyStatusQuery(
        int year,
        int month,
        int rank,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score
) {

    public static ClubMonthlyStatusQuery from(int year, int month, ClubFeedRankingQuery ranking) {
        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(ranking.rank())
                .feedCount(ranking.feedCount())
                .viewCount(ranking.viewCount())
                .likeCount(ranking.likeCount())
                .commentCount(ranking.commentCount())
                .score(ranking.score())
                .build();
    }

    public static ClubMonthlyStatusQuery createEmpty(int year, int month) {
        return ClubMonthlyStatusQuery.builder()
                .year(year)
                .month(month)
                .rank(0)
                .feedCount(0)
                .viewCount(0)
                .likeCount(0)
                .commentCount(0)
                .score(0)
                .build();
    }
}
