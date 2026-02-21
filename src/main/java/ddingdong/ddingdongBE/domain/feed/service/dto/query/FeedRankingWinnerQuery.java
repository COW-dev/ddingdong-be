package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.repository.dto.FeedRankingWinnerDto;
import lombok.Builder;

@Builder
public record FeedRankingWinnerQuery(
        String clubName,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score,
        int targetYear,
        int targetMonth
) {

    public static FeedRankingWinnerQuery from(FeedRankingWinnerDto dto) {
        return FeedRankingWinnerQuery.builder()
                .clubName(dto.getClubName())
                .feedCount(dto.getFeedCount())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .score(dto.getScore())
                .targetYear(dto.getTargetYear())
                .targetMonth(dto.getTargetMonth())
                .build();
    }
}
