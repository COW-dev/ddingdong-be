package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import ddingdong.ddingdongBE.domain.feed.repository.dto.MonthlyFeedRankingDto;
import lombok.Builder;

@Builder
public record ClubFeedRankingQuery(
        int rank,
        Long clubId,
        String clubName,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score
) {

    public static ClubFeedRankingQuery of(int rank, MonthlyFeedRankingDto dto, long score) {
        return ClubFeedRankingQuery.builder()
                .rank(rank)
                .clubId(dto.getClubId())
                .clubName(dto.getClubName())
                .feedCount(dto.getFeedCount())
                .viewCount(dto.getViewCount())
                .likeCount(dto.getLikeCount())
                .commentCount(dto.getCommentCount())
                .score(score)
                .build();
    }
}
