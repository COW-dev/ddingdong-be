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

    public static List<AdminClubFeedRankingResponse> from(List<ClubFeedRankingQuery> queries) {
        return queries.stream()
                .map(query -> AdminClubFeedRankingResponse.builder()
                        .rank(query.rank())
                        .clubName(query.clubName())
                        .feedScore(query.feedScore())
                        .viewScore(query.viewScore())
                        .likeScore(query.likeScore())
                        .commentScore(query.commentScore())
                        .totalScore(query.totalScore())
                        .build())
                .toList();
    }
}
