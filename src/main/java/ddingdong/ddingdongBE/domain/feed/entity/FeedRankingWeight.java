package ddingdong.ddingdongBE.domain.feed.entity;

// 피드 월별 랭킹 점수 가중치. 스냅샷 저장(FeedMonthlyRanking)과 실시간 조회(GeneralFeedRankingService)가
// 동일한 기준으로 점수를 계산하도록 가중치를 한 곳에서만 정의한다.
public final class FeedRankingWeight {

    public static final int FEED = 10;
    public static final int VIEW = 3;
    public static final int LIKE = 1;
    public static final int COMMENT = 5;

    private FeedRankingWeight() {
    }
}
