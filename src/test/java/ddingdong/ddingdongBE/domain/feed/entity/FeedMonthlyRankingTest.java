package ddingdong.ddingdongBE.domain.feed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import ddingdong.ddingdongBE.common.fixture.FeedMonthlyRankingFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FeedMonthlyRankingTest {

    @DisplayName("calculateScore - 가중치 공식에 따라 점수를 계산한다")
    @Test
    void calculateScore_appliesWeightFormula() {
        // given
        // feedCount=10, viewCount=100, likeCount=50, commentCount=20
        // score = 10*10 + 100*1 + 50*3 + 20*5 = 100 + 100 + 150 + 100 = 450
        FeedMonthlyRanking ranking = FeedMonthlyRankingFixture.create(
                1L, "테스트 동아리", 10, 100, 50, 20, 2025, 1, 1);

        // when
        long score = ranking.calculateScore();

        // then
        assertThat(score).isEqualTo(450L);
    }

    @DisplayName("calculateScore - 모든 카운트가 0이면 점수는 0이다")
    @Test
    void calculateScore_allZero() {
        // given
        FeedMonthlyRanking ranking = FeedMonthlyRankingFixture.create(
                1L, "빈 동아리", 0, 0, 0, 0, 2025, 1, 1);

        // when
        long score = ranking.calculateScore();

        // then
        assertThat(score).isEqualTo(0L);
    }

    @DisplayName("빌더로 생성 시 score가 자동 계산된다")
    @Test
    void builder_calculatesScoreAutomatically() {
        // given & when
        FeedMonthlyRanking ranking = FeedMonthlyRanking.builder()
                .clubId(1L)
                .clubName("테스트")
                .feedCount(5)
                .viewCount(50)
                .likeCount(10)
                .commentCount(3)
                .targetYear(2025)
                .targetMonth(3)
                .build();

        // then
        // score = 5*10 + 50*1 + 10*3 + 3*5 = 50 + 50 + 30 + 15 = 145
        assertThat(ranking.getScore()).isEqualTo(145L);
    }

    @DisplayName("assignRanking - 순위를 할당한다")
    @Test
    void assignRanking_setsRanking() {
        // given
        FeedMonthlyRanking ranking = FeedMonthlyRanking.builder()
                .clubId(1L)
                .clubName("테스트")
                .feedCount(1)
                .viewCount(1)
                .likeCount(1)
                .commentCount(1)
                .targetYear(2025)
                .targetMonth(1)
                .build();

        // when
        ranking.assignRanking(3);

        // then
        assertThat(ranking.getRanking()).isEqualTo(3);
    }
}
