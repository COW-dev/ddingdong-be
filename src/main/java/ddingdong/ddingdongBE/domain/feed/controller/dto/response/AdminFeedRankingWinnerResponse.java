package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import lombok.Builder;

@Builder
public record AdminFeedRankingWinnerResponse(
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

    public static AdminFeedRankingWinnerResponse from(FeedRankingWinnerQuery query) {
        return AdminFeedRankingWinnerResponse.builder()
                .clubId(query.clubId())
                .clubName(query.clubName())
                .feedCount(query.feedCount())
                .viewCount(query.viewCount())
                .likeCount(query.likeCount())
                .commentCount(query.commentCount())
                .score(query.score())
                .targetYear(query.targetYear())
                .targetMonth(query.targetMonth())
                .build();
    }
}
