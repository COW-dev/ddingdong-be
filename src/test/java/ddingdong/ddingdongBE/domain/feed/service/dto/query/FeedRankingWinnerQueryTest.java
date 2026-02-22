package ddingdong.ddingdongBE.domain.feed.service.dto.query;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import ddingdong.ddingdongBE.domain.feed.entity.FeedMonthlyRanking;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedRankingWinnerQueryTest {

    @DisplayName("from - FeedMonthlyRanking 엔티티의 모든 필드를 올바르게 매핑한다")
    @Test
    void from_mapsAllFields() {
        // given
        FeedMonthlyRanking entity = FeedMonthlyRankingFixture.create(
                1L, "테스트 동아리", 10, 100, 50, 20, 2025, 3, 1);

        // when
        FeedRankingWinnerQuery query = FeedRankingWinnerQuery.from(entity);

        // then
        assertSoftly(softly -> {
            softly.assertThat(query.clubName()).isEqualTo("테스트 동아리");
            softly.assertThat(query.feedCount()).isEqualTo(10);
            softly.assertThat(query.viewCount()).isEqualTo(100);
            softly.assertThat(query.likeCount()).isEqualTo(50);
            softly.assertThat(query.commentCount()).isEqualTo(20);
            softly.assertThat(query.score()).isEqualTo(450L);
            softly.assertThat(query.targetYear()).isEqualTo(2025);
            softly.assertThat(query.targetMonth()).isEqualTo(3);
        });
    }
}
