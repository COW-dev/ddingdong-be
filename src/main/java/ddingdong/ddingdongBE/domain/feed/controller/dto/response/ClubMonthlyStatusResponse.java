package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.ClubMonthlyStatusQuery;
import lombok.Builder;

@Builder
public record ClubMonthlyStatusResponse(
        int year,
        int month,
        int rank,
        long feedCount,
        long viewCount,
        long likeCount,
        long commentCount,
        long score
) {

    public static ClubMonthlyStatusResponse from(ClubMonthlyStatusQuery query) {
        return ClubMonthlyStatusResponse.builder()
                .year(query.year())
                .month(query.month())
                .rank(query.rank())
                .feedCount(query.feedCount())
                .viewCount(query.viewCount())
                .likeCount(query.likeCount())
                .commentCount(query.commentCount())
                .score(query.score())
                .build();
    }
}
