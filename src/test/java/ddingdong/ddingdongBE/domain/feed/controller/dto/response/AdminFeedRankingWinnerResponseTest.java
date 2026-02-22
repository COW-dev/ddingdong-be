package ddingdong.ddingdongBE.domain.feed.controller.dto.response;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.domain.feed.service.dto.query.FeedRankingWinnerQuery;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AdminFeedRankingWinnerResponseTest {

    @DisplayName("from - FeedRankingWinnerQuery의 모든 필드를 올바르게 매핑한다")
    @Test
    void from_mapsAllFields() {
        // given
        FeedRankingWinnerQuery query = FeedRankingWinnerQuery.builder()
                .clubName("테스트 동아리")
                .feedCount(10)
                .viewCount(100)
                .likeCount(50)
                .commentCount(20)
                .score(450)
                .targetYear(2025)
                .targetMonth(3)
                .build();

        // when
        AdminFeedRankingWinnerResponse response = AdminFeedRankingWinnerResponse.from(query);

        // then
        assertSoftly(softly -> {
            softly.assertThat(response.clubName()).isEqualTo("테스트 동아리");
            softly.assertThat(response.feedCount()).isEqualTo(10);
            softly.assertThat(response.viewCount()).isEqualTo(100);
            softly.assertThat(response.likeCount()).isEqualTo(50);
            softly.assertThat(response.commentCount()).isEqualTo(20);
            softly.assertThat(response.score()).isEqualTo(450);
            softly.assertThat(response.targetYear()).isEqualTo(2025);
            softly.assertThat(response.targetMonth()).isEqualTo(3);
        });
    }
}
