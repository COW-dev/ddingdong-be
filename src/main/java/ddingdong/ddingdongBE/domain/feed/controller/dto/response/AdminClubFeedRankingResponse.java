package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubFeedRankingQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminClubFeedRankingResponse(
        @Schema(description = "순위", example = "1")
        int rank,
        @Schema(description = "동아리 이름", example = "카우")
        String clubName,
        @Schema(description = "피드 가중치 점수", example = "150")
        long feedScore,
        @Schema(description = "조회 가중치 점수", example = "300")
        long viewScore,
        @Schema(description = "좋아요 가중치 점수", example = "50")
        long likeScore,
        @Schema(description = "댓글 가중치 점수", example = "100")
        long commentScore,
        @Schema(description = "총 가중치 점수", example = "600")
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
