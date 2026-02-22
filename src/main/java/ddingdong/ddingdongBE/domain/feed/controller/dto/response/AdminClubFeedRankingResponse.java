package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubFeedRankingResponse(
        int rank,
        Long clubId,
        String clubName,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score
) {

    public static List<AdminClubFeedRankingResponse> from(List<ClubFeedRankingQuery> queries) {
        return queries.stream()
                .map(query -> AdminClubFeedRankingResponse.builder()
                        .rank(query.rank())
                        .clubId(query.clubId())
                        .clubName(query.clubName())
                        .feedCount(query.feedCount())
                        .viewCount(query.viewCount())
                        .likeCount(query.likeCount())
                        .commentCount(query.commentCount())
                        .score(query.score())
                        .build())
                .toList();
    }
}
