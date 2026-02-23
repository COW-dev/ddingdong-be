package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubFeedRankingResponse(
        int rank,
        String clubName,
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

    public static List<AdminClubFeedRankingResponse> from(List<ClubFeedRankingQuery> queries) {
        return queries.stream()
                .map(query -> AdminClubFeedRankingResponse.builder()
                        .rank(query.rank())
                        .clubName(query.clubName())
                        .feedScore(query.feedCount() * FEED_WEIGHT)
                        .viewScore(query.viewCount() * VIEW_WEIGHT)
                        .likeScore(query.likeCount() * LIKE_WEIGHT)
                        .commentScore(query.commentCount() * COMMENT_WEIGHT)
                        .totalScore(query.score())
                        .build())
                .toList();
    }
}
