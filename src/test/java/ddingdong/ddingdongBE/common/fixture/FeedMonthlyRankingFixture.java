package ddingdong.ddingdongBE.common.fixture;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;

public class FeedMonthlyRankingFixture {

    public static FeedMonthlyRanking create(Long clubId, String clubName,
            long feedCount, long viewCount, long likeCount, long commentCount,
            int targetYear, int targetMonth, int ranking) {
        FeedMonthlyRanking entity = FeedMonthlyRanking.builder()
                .clubId(clubId)
                .clubName(clubName)
                .feedCount(feedCount)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .commentCount(commentCount)
                .targetYear(targetYear)
                .targetMonth(targetMonth)
                .build();
        entity.assignRanking(ranking);
        return entity;
    }

    public static FeedMonthlyRanking createWinner(Long clubId, String clubName,
            int targetYear, int targetMonth) {
        return create(clubId, clubName, 10, 100, 50, 20, targetYear, targetMonth, 1);
    }

    public static FeedMonthlyRanking createWithRanking(Long clubId, String clubName,
            int targetYear, int targetMonth, int ranking) {
        return create(clubId, clubName, 5, 50, 25, 10, targetYear, targetMonth, ranking);
    }
}
