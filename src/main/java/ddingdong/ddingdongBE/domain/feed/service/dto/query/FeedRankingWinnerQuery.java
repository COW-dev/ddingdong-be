package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import lombok.Builder;

@Builder
public record FeedRankingWinnerQuery(
        Long clubId,
        String clubName,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score,
        int targetYear,
        int targetMonth
) {

    public static FeedRankingWinnerQuery from(FeedMonthlyRanking entity) {
        return FeedRankingWinnerQuery.builder()
                .clubId(entity.getClubId())
                .clubName(entity.getClubName())
                .feedCount(entity.getFeedCount())
                .viewCount(entity.getViewCount())
                .likeCount(entity.getLikeCount())
                .commentCount(entity.getCommentCount())
                .score(entity.getScore())
                .targetYear(entity.getTargetYear())
                .targetMonth(entity.getTargetMonth())
                .build();
    }
}
