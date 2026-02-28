package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import lombok.Builder;

@Builder
public record ClubFeedRankingQuery(
        int rank,
        Long clubId,
        String clubName,
        long feedScore,
        long viewScore,
        long likeScore,
        long commentScore,
        long totalScore
) {

    public static ClubFeedRankingQuery of(int rank, Long clubId, String clubName,
            long feedScore, long viewScore, long likeScore, long commentScore, long totalScore) {
        return ClubFeedRankingQuery.builder()
                .rank(rank)
                .clubId(clubId)
                .clubName(clubName)
                .feedScore(feedScore)
                .viewScore(viewScore)
                .likeScore(likeScore)
                .commentScore(commentScore)
                .totalScore(totalScore)
                .build();
    }
}
